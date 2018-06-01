package com.ht.eventbus.response;

import com.ht.eventbus.Request;
import com.ht.eventbus.Response;
import com.ht.eventbus.request.RequestBean;

/**
 * Author Leiht
 * Date 2018-05-25
 */
public abstract class ResponseMake {

    protected abstract Object invokeMethod()  ;

    protected abstract void setMethod(RequestBean requestBean);

    public Response makeResponse(Request request) {
        return null;
    }
}
