package com.ht.eventbus.core;


import android.content.Context;

import com.google.gson.Gson;
import com.ht.eventbus.Request;
import com.ht.eventbus.Response;
import com.ht.eventbus.annotion.ClassId;
import com.ht.eventbus.request.RequestBean;
import com.ht.eventbus.request.RequestParameter;
import com.ht.eventbus.service.HermesService;
import com.ht.eventbus.util.TypeUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

/**
 * Author Leiht
 * Date 2018-05-25
 */
public class Hermes {
    //得到对象
    public static final int TYPE_NEW = 0;
    //得到单例
    public static final int TYPE_GET = 1;

    /**
     * Gson对象
     */
    Gson GSON = new Gson();

    private TypeCenter typeCenter;

    private Context mContext;

    private ServiceConnectionManager serviceConnectionManager;

    /**
     * 单例对象
     */
    protected volatile static Hermes defaultInstance;

    /**
     * Single method
     *
     * @return
     */
    public static Hermes getDefault() {
        Hermes instance = defaultInstance;
        if (instance == null) {
            synchronized (Hermes.class) {
                instance = Hermes.defaultInstance;
                if (instance == null) {
                    instance = Hermes.defaultInstance = new Hermes();
                }
            }
        }
        return instance;
    }

    private Hermes() {
        serviceConnectionManager = ServiceConnectionManager.getInstance();
    }

    //------------------------------Server进程----------------------------------------------------------------
    public void register(Class<?> clazz) {
        //注册，保存对象及方法到Map
        typeCenter.register(clazz);
    }

    //------------------------------Client进程----------------------------------------------------------------
    public void connect(Context context, Class<? extends HermesService> service) {
        connectApp(context, null, service);
    }

    public void connectApp(Context context, String packageName, Class<? extends HermesService> service) {
        init(context);
        serviceConnectionManager.bind(context.getApplicationContext(), packageName, service);
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        typeCenter = TypeCenter.getInstance();
    }

    public <T> T getObject(Class<T> clazz, String methodName, Object... parameters) {
        try {
            //相当于做了一个检测，并没有从其它进程中获取信息
            Method method = null;
            if(parameters == null || parameters.length <= 0) {
                method = clazz.getMethod(methodName);
            }else {
                Class<?>[] parameterTypes = new Class<?>[parameters.length];
                for(int i = 0; i < parameterTypes.length; i++) {
                    Object object = parameters[i];
                    Class<?> parameterType = object.getClass();
                    parameterTypes[i] = parameterType;
                }
                method = clazz.getMethod(methodName, parameterTypes);
            }

            Response response = sendRequest(HermesService.class, clazz, method, parameters);
            //返回在本进程的代理对象
            return getProxy(HermesService.class, clazz);
        }catch (Exception e) {
            return null;
        }
    }

    /**
     * 发生在客户端，夸进程获取单例对象
     *
     * @param clazz
     * @param parameters
     * @param <T>
     * @return
     */
    public <T> T getInstance(Class<T> clazz, Object... parameters) {
        //相当于做了一个检测，并没有从其它进程中获取信息
        Response response = sendRequest(HermesService.class, clazz, null, parameters);
        //返回在本进程的代理对象
        return getProxy(HermesService.class, clazz);
    }

    /**
     * 获取代理对象
     *
     * @param service
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> T getProxy(Class<? extends HermesService> service, Class clazz) {
        ClassLoader classLoader = service.getClassLoader();
        T proxy = (T) Proxy.newProxyInstance(classLoader, new Class<?>[]{clazz}, new HermesInvocationHandler(service, clazz));
        return proxy;
    }

    private <T> Response sendRequest(Class<HermesService> hermesServiceClass
            , Class<T> clazz, Method method, Object[] parameters) {
        RequestBean requestBean = new RequestBean();

        String className = null;
        if (clazz.getAnnotation(ClassId.class) == null) {
            requestBean.setClassName(clazz.getName());
            requestBean.setResultClassName(clazz.getName());
        } else {
            //返回类型的全类名
            requestBean.setClassName(clazz.getAnnotation(ClassId.class).value());
            requestBean.setResultClassName(clazz.getAnnotation(ClassId.class).value());
        }
        if (method != null) {
            requestBean.setMethodName(TypeUtils.getMethodId(method));
        }
        RequestParameter[] requestParameters = null;
        if (parameters != null && parameters.length > 0) {
            requestParameters = new RequestParameter[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Object parameter = parameters[i];
                String parameterClassName = parameter.getClass().getName();
                String parameterValue = GSON.toJson(parameter);

                RequestParameter requestParameter = new RequestParameter(parameterClassName, parameterValue);
                requestParameters[i] = requestParameter;
            }
        }

        if (requestParameters != null) {
            requestBean.setRequestParameter(requestParameters);
        }
        Request request = new Request(GSON.toJson(requestBean), TYPE_GET);
        return serviceConnectionManager.request(hermesServiceClass, request);
    }

    public <T> Response sendObjectRequest(Class<HermesService> hermesServiceClass
            , Class<T> clazz, Method method, Object[] parameters) {
        RequestBean requestBean = new RequestBean();

        String className = null;
        if (clazz.getAnnotation(ClassId.class) == null) {
            requestBean.setClassName(clazz.getName());
            requestBean.setResultClassName(clazz.getName());
        } else {
            requestBean.setClassName(clazz.getAnnotation(ClassId.class).value());
            requestBean.setResultClassName(clazz.getAnnotation(ClassId.class).value());
        }
        if (method != null) {
            requestBean.setMethodName(TypeUtils.getMethodId(method));
        }
        RequestParameter[] requestParameters = null;
        if (parameters != null && parameters.length > 0) {
            requestParameters = new RequestParameter[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Object parameter = parameters[i];
                String parameterClassName = parameter.getClass().getName();
                String parameterValue = GSON.toJson(parameter);

                RequestParameter requestParameter = new RequestParameter(parameterClassName, parameterValue);
                requestParameters[i] = requestParameter;
            }
        }
        if (requestParameters != null) {
            requestBean.setRequestParameter(requestParameters);
        }
        Request request = new Request(GSON.toJson(requestBean), TYPE_NEW);
        return serviceConnectionManager.request(hermesServiceClass, request);
    }
}
