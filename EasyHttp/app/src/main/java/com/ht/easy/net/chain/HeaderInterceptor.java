package com.ht.easy.net.chain;

import com.ht.easy.net.Request;
import com.ht.easy.net.RequestBody;
import com.ht.easy.net.Response;

import java.io.IOException;
import java.util.Map;

/**
 * @author Leiht
 * @date 2018/5/3
 */
public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(InterceptorChain chain) throws IOException {

        Request request = chain.call().request();
        Map<String, String> headers = request.headers();

        //如果使用者没有配置 Connection请求头
        if(!headers.containsKey("Connection")) {
            headers.put("Connection", "Keep-Alive");
        }

        if(!headers.containsKey("Host")) {
            headers.put("Host", request.url().getHost());
        }

        //是否有请求体
        if(request.body() != null) {
            RequestBody body = request.body();
            long contentLength = body.contentLength();
            //请求体长度
            if (contentLength != 0) {
                headers.put("Content-Length",String.valueOf(contentLength));
            }
            String contentType = body.contentType();
            if (contentType != null){
                headers.put("Content-Type",contentType);
            }
        }

        //责任链中的下一个
        return chain.process();
    }
}
