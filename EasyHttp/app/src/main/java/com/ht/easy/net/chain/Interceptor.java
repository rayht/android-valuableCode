package com.ht.easy.net.chain;

import com.ht.easy.net.Response;

import java.io.IOException;

/**
 * @author Leiht
 * @date 2018/5/3
 */
public interface Interceptor {
    Response intercept(InterceptorChain chain) throws IOException;
}
