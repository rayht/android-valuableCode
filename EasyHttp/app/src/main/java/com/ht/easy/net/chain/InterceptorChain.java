package com.ht.easy.net.chain;

import com.ht.easy.net.Call;
import com.ht.easy.net.HttpConnection;
import com.ht.easy.net.Response;

import java.io.IOException;
import java.util.List;

/**
 * @author Leiht
 * @date 2018/5/3
 */
public class InterceptorChain {

    /**
     * 所有的拦截器
     */
    private List<Interceptor> interceptors;

    private int index;

    private Call call;

    HttpConnection connection;

    public InterceptorChain(List<Interceptor> interceptors, int index, Call call, HttpConnection connection) {
        this.interceptors = interceptors;
        this.index = index;
        this.call = call;
        this.connection = connection;
    }

    public Call call() {
        return call;
    }

    public Response process(HttpConnection connection) throws IOException {
        this.connection = connection;
        return process();
    }

    public Response process() throws IOException {
        Interceptor interceptor = interceptors.get(index);
        InterceptorChain nextChain = new InterceptorChain(interceptors, index + 1, call, connection);
        Response response = interceptor.intercept(nextChain);
        return response;
    }
}
