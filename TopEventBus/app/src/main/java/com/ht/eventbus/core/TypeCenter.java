package com.ht.eventbus.core;

import com.ht.eventbus.EventBus;
import com.ht.eventbus.util.TypeUtils;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author Leiht
 * Date 2018-05-25
 */
public class TypeCenter {
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
}
