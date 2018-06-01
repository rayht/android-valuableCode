package com.ht.eventbus.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.ht.eventbus.EventBusService;
import com.ht.eventbus.Request;
import com.ht.eventbus.Response;
import com.ht.eventbus.service.HermesService;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Author Leiht
 * Date 2018-05-25
 */
public class ServiceConnectionManager {

    /**
     * 单例对象
     */
    protected volatile static ServiceConnectionManager defaultInstance;

    //保存Binder对象
    private final ConcurrentHashMap<Class<? extends HermesService>, EventBusService> mHermesServices;

    //Class对应的链接对象
    private final ConcurrentHashMap<Class<? extends HermesService>, HermesServiceConnection> mHermesServiceConnections;


    public static ServiceConnectionManager getInstance() {
        ServiceConnectionManager instance = defaultInstance;
        if (instance == null) {
            synchronized (Hermes.class) {
                instance = ServiceConnectionManager.defaultInstance;
                if (instance == null) {
                    instance = ServiceConnectionManager.defaultInstance = new ServiceConnectionManager();
                }
            }
        }
        return instance;
    }

    private ServiceConnectionManager() {
        mHermesServices = new ConcurrentHashMap<Class<? extends HermesService>, EventBusService>();
        mHermesServiceConnections = new ConcurrentHashMap<Class<? extends HermesService>, HermesServiceConnection>();
    }

    public void bind(Context context, String packageName, Class<? extends HermesService> service) {
        HermesServiceConnection connection = new HermesServiceConnection(service);
        mHermesServiceConnections.put(service, connection);
        Intent intent;
        if (TextUtils.isEmpty(packageName)) {
            intent = new Intent(context, service);
        } else {
            intent = new Intent();
            intent.setClassName(packageName, service.getName());
        }
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public Response request(Class<HermesService> hermesServiceClass, Request request) {
        EventBusService eventBusService = mHermesServices.get(hermesServiceClass);
        if (eventBusService != null) {
            try {
                Response responce = eventBusService.send(request);
                return responce;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private class HermesServiceConnection implements ServiceConnection {
        private Class<? extends HermesService> mClass;

        HermesServiceConnection(Class<? extends HermesService> service) {
            mClass = service;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            EventBusService hermesService = EventBusService.Stub.asInterface(service);
            mHermesServices.put(mClass, hermesService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mHermesServices.remove(mClass);
        }
    }

}
