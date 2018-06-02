package com.ht.eventbus.core;

import android.text.TextUtils;
import android.util.Log;

import com.ht.eventbus.EventBus;
import com.ht.eventbus.request.RequestBean;
import com.ht.eventbus.request.RequestParameter;
import com.ht.eventbus.util.TypeUtils;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author Leiht
 * Date 2018-05-25
 */
public class TypeCenter {

    private static final String TAG = "TypeCenter";

    /**
     * 单例对象
     */
    protected volatile static TypeCenter defaultInstance;

    /**
     * name : Class
     */
    private final ConcurrentHashMap<String, Class<?>> mAnnotatedClasses;

    /**
     * Class : ConcurrentHashMap<String : Method>
     */
    private final ConcurrentHashMap<Class<?>, ConcurrentHashMap<String, Method>> mRawMethods;

    private TypeCenter() {
        mAnnotatedClasses = new ConcurrentHashMap<String, Class<?>>();
        mRawMethods = new ConcurrentHashMap<Class<?>, ConcurrentHashMap<String, Method>>();
    }

    /**
     * Single method
     *
     * @return
     */
    public static TypeCenter getInstance() {
        TypeCenter instance = defaultInstance;
        if (instance == null) {
            synchronized (TypeCenter.class) {
                instance = TypeCenter.defaultInstance;
                if (instance == null) {
                    instance = TypeCenter.defaultInstance = new TypeCenter();
                }
            }
        }
        return instance;
    }

    public void register(Class<?> clazz) {
        //分为注册类
        registerClass(clazz);
        //注册方法
        registerMethod(clazz);
    }

    private void registerClass(Class<?> clazz) {
        String className = clazz.getName();
        mAnnotatedClasses.putIfAbsent(className, clazz);
    }

    private void registerMethod(Class<?> clazz) {
        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            mRawMethods.putIfAbsent(clazz, new ConcurrentHashMap<String, Method>());
            ConcurrentHashMap<String, Method> map = mRawMethods.get(clazz);
            String key = TypeUtils.getMethodId(method);
            map.put(key, method);
        }
    }

    public Method getMethod(Class<?> clazz, RequestBean requestBean) {
        String name = requestBean.getMethodName();
        if ( name!= null) {
            Log.i(TAG, "getMethod: "+name);
            mRawMethods.putIfAbsent(clazz, new ConcurrentHashMap<String, Method>());
            ConcurrentHashMap<String, Method> methods = mRawMethods.get(clazz);
            Method method = methods.get(name);
            if (method != null) {
                Log.i(TAG, "getMethod: "+method.getName());
                return method;
            }
            int pos = name.indexOf('(');

            Class[] paramters = null;
            RequestParameter[] requestParameters = requestBean.getRequestParameter();
            if (requestParameters != null && requestParameters.length > 0) {
                paramters = new Class[requestParameters.length];
                for (int i=0;i<requestParameters.length;i++) {
                    paramters[i]=getClassType(requestParameters[i].getParameterClassName());
                }
            }
            method = TypeUtils.getMethod(clazz, name.substring(0, pos), paramters);
            methods.put(name, method);
            return method;
        }
        return null;
    }
    public Class<?> getClassType(String name)   {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        Class<?> clazz = mAnnotatedClasses.get(name);
        if (clazz == null) {
            try {
                clazz = Class.forName(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return clazz;
    }
}
