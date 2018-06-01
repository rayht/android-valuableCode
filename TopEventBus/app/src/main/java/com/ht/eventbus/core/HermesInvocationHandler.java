package com.ht.eventbus.core;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.ht.eventbus.Response;
import com.ht.eventbus.response.ResponseBean;
import com.ht.eventbus.service.HermesService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Author Leiht
 * Date 2018-05-25
 */
public class HermesInvocationHandler implements InvocationHandler {

    private Class clazz;
    private static final Gson GSON = new Gson();
    private Class hermeService;

    public HermesInvocationHandler(Class<? extends HermesService> service, Class clazz) {
        this.clazz = clazz;
        this.hermeService = service;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Response responce = Hermes.getDefault().sendObjectRequest(hermeService, clazz, method, args);
        if (!TextUtils.isEmpty(responce.getData())) {
            ResponseBean responceBean = GSON.fromJson(responce.getData(), ResponseBean.class);
            if (responceBean.getData() != null) {
                Object getUserReslut = responceBean.getData();//"davaid:123456"
                String data = GSON.toJson(getUserReslut);
                Class stringgetUser = method.getReturnType();
                Object o = GSON.fromJson(data, stringgetUser);
                return o;

            }
        }
        return null;
    }
}
