package com.ht.eventbus.core;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Author Leiht
 * Date 2018-05-25
 */
public class ObjectCenter {

    /**
     * Log Tag
     */
    private static final String TAG = "ObjectCenter";

    /**
     * 单例对象
     */
    private static volatile ObjectCenter sInstance = null;

    private final ConcurrentHashMap<String, Object> mObjects;

    private ObjectCenter() {
        mObjects = new ConcurrentHashMap<String, Object>();
    }

    public static ObjectCenter getInstance() {
        if (sInstance == null) {
            synchronized (ObjectCenter.class) {
                if (sInstance == null) {
                    sInstance = new ObjectCenter();
                }
            }
        }
        return sInstance;
    }

    public Object getObject(String name) {
        return mObjects.get(name);
    }

    public void putObject(String name, Object object) {
        mObjects.put(name, object);
    }

}

