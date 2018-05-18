package com.ht.easy.net.chain;

import android.util.Log;

import com.ht.easy.net.Call;
import com.ht.easy.net.HttpClient;
import com.ht.easy.net.Response;

import java.io.IOException;

/**
 * @author Leiht
 * @date 2018/5/3
 */
public class RetryInterceptor implements Interceptor {
    @Override
    public Response intercept(InterceptorChain chain) throws IOException {
        Log.d("TAG", "Retry interceptor");
        Call call = chain.call();
        HttpClient client = call.client();
        int retrys = client.retyrs();
        IOException exception = null;
        for (int i = 0; i < retrys + 1; i++) {
            if (call.isCanceled()) {
                throw new IOException("This http call has been canceled");
            }
            try {
                Response response = chain.process();
                return response;
            } catch (IOException e) {
                exception = e;
            }
        }
        throw exception;
    }
}
