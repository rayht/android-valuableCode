package com.ht.eventbus.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.ht.eventbus.EventBusService;
import com.ht.eventbus.Request;
import com.ht.eventbus.Response;
import com.ht.eventbus.core.Hermes;
import com.ht.eventbus.response.InstanceResponceMake;
import com.ht.eventbus.response.ObjectResponceMake;
import com.ht.eventbus.response.ResponseMake;

/**
 * Author Leiht
 * Date 2018-05-25
 */
public class HermesService extends Service {

    /**
     * Binder Stub对象
     */
    private EventBusService.Stub mBinder = new EventBusService.Stub() {
        @Override
        public Response send(Request request) throws RemoteException {
            //对请求参数进行处理  生成Responce结果返回
            ResponseMake responceMake = null;
            switch (request.getType()) {
                case Hermes.TYPE_GET:
                    responceMake = new InstanceResponceMake();
                    break;
                case Hermes.TYPE_NEW:
                    responceMake = new ObjectResponceMake();
                    break;
            }
            return responceMake.makeResponse(request);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public static class HermesService0 extends HermesService {
    }

    public static class HermesService1 extends HermesService {
    }

    public static class HermesService2 extends HermesService {
    }

    public static class HermesService3 extends HermesService {
    }

    public static class HermesService4 extends HermesService {
    }

    public static class HermesService5 extends HermesService {
    }

    public static class HermesService6 extends HermesService {
    }

    public static class HermesService7 extends HermesService {
    }

    public static class HermesService8 extends HermesService {
    }

    public static class HermesService9 extends HermesService {
    }
}
