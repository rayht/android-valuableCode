package com.ht.easy.net.chain;

import android.util.Log;

import com.ht.easy.net.HttpClient;
import com.ht.easy.net.HttpConnection;
import com.ht.easy.net.HttpUrl;
import com.ht.easy.net.Request;
import com.ht.easy.net.Response;

import java.io.IOException;

/**
 * @author Leiht
 * @date 2018/5/3
 */
public class ConnectionInterceptor implements Interceptor {
    @Override
    public Response intercept(InterceptorChain chain) throws IOException {
        Log.e("interceptor", "获取连接拦截器");

        Request request = chain.call().request();
        HttpClient client = chain.call().client();
        HttpUrl url = request.url();
        //从连接池中获得连接
        HttpConnection connection = client.connectionPool().get(url.getHost(), url.getPort());
        if(connection == null) {
            connection = new HttpConnection();
        }else {
            Log.e("interceptor", "从连接池中获得连接");
        }
        connection.setRequest(request);

        //执行下一个拦截器
        try {
            Response response = chain.process(connection);
            if (response.isKeepAlive()) {
                client.connectionPool().put(connection);
            }else{
                connection.close();
            }
            return response;
        } catch (IOException e) {
            connection.close();
            throw e;
        }
    }
}
