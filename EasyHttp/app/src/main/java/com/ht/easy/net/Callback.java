package com.ht.easy.net;

/**
 * @author Leiht
 * @date 2018/5/3
 */
public interface Callback {

    void onFailure(Call call, Throwable throwable);

    void onResponse(Call call, Response response);

}
