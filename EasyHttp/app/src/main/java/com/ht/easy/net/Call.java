package com.ht.easy.net;

import android.content.res.Resources;

import com.ht.easy.net.chain.CallServiceInterceptor;
import com.ht.easy.net.chain.ConnectionInterceptor;
import com.ht.easy.net.chain.HeaderInterceptor;
import com.ht.easy.net.chain.Interceptor;
import com.ht.easy.net.chain.InterceptorChain;
import com.ht.easy.net.chain.RetryInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author Leiht
 * @date 2018/5/3
 */
public class Call {

    /**
     * 请求
     */
    Request request;

    /**
     * HttpClient对象
     */
    HttpClient client;

    /**
     * 是否执行过
     */
    boolean executed;

    /**
     * 是否取消
     */
    boolean canceled;

    public Call(HttpClient client, Request request) {
        this.client = client;
        this.request = request;
    }

    public Request request() {
        return request;
    }

    public HttpClient client() {
        return client;
    }

    /**
     * 取消当前Call
     */
    public void cancel() {
        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void enqueue(Callback callback) {
        synchronized (this) {
            if(executed) {
                //不能重复执行
                throw new IllegalStateException("Already Executed");
            }
            executed = true;

            client.dispatcher().enqueue(new AsyncCall(callback));
        }
    }

    final class AsyncCall implements Runnable {

        private Callback callback;

        public AsyncCall(Callback callback) {
            this.callback = callback;
        }

        @Override
        public void run() {
            //是否已经通知过callback
            boolean signalledCallback = false;
            try {
                Response response = getResponse();

                if(canceled) {
                    signalledCallback = true;
                    callback.onFailure(Call.this, new IOException("This http call has been canceled"));
                }else {
                    signalledCallback = true;
                    callback.onResponse(Call.this, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if(!signalledCallback) {
                    callback.onFailure(Call.this, e);
                }
            }finally {
                client.dispatcher().finished(this);
            }
        }

        public String host() {
            return request.url().getHost();
        }
    }

    private Response getResponse() throws Exception {
        List<Interceptor> interceptors = new ArrayList<>();
        List<Interceptor> customInterceptors = client.interceptors();

//        interceptors.addAll(client.interceptors());

        //重试拦截器
        interceptors.add(new RetryInterceptor());
        //请求头拦截器
        interceptors.add(new HeaderInterceptor());
        //连接拦截器
        interceptors.add(new ConnectionInterceptor());
        //通信拦截器
        interceptors.add(new CallServiceInterceptor());

        InterceptorChain chain = new InterceptorChain(interceptors, 0, this, null);

        return chain.process();
    }
}
