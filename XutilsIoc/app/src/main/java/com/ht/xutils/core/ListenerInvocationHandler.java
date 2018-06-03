package com.ht.xutils.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ListenerInvocationHandler implements InvocationHandler {

    private Object activity;

    private Method callbackMethod;

    public ListenerInvocationHandler(Object activity, Method callbackMethod) {
        this.activity = activity;
        this.callbackMethod = callbackMethod;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return callbackMethod.invoke(activity, args);
    }
}
