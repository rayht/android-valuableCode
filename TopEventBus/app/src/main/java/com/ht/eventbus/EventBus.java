package com.ht.eventbus;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author Leiht
 * Date 2018-05-25
 */
public class EventBus {

    /**
     * Logger TAG
     */
    public static final String TAG = "TopEventBus";

    /**
     * 单例对象
     */
    protected volatile static EventBus defaultInstance;

    //保存方法map  post(一个对象),保存该对象中所有接收消息的方法
    private Map<Object, List<SubscribleMethod>> cacheMap;

    /**
     * 线程池
     */
    private ExecutorService executorService;

    /**
     * 主线程Handler
     */
    private Handler mHandler;

    /**
     * Single method
     *
     * @return
     */
    public static EventBus getDefault() {
        EventBus instance = defaultInstance;
        if (instance == null) {
            synchronized (EventBus.class) {
                instance = EventBus.defaultInstance;
                if (instance == null) {
                    instance = EventBus.defaultInstance = new EventBus();
                }
            }
        }
        return instance;
    }

    private EventBus() {
        this.cacheMap = new HashMap<>();
        executorService = Executors.newCachedThreadPool();
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 注册对象，即保存对象中标记有SubscrbleMethod的方法
     *
     * @param subscriber
     */
    public void register(Object subscriber) {
        Class clazz = subscriber.getClass();
        List<SubscribleMethod> subscribleMethods = cacheMap.get(subscriber);
        if (subscribleMethods == null) {
            subscribleMethods = getSuscrbleMethods(subscriber);
            cacheMap.put(subscriber, subscribleMethods);
        }
    }

    private List<SubscribleMethod> getSuscrbleMethods(Object subscriber) {
        List<SubscribleMethod> subscribleMethods = new ArrayList<>();
        Class clazz = subscriber.getClass();

        //递归取其父类的subscriber方法，直到framework的类为止
        while (clazz != null) {
            String clazzName = clazz.getName();
            if (clazzName.startsWith("java.") || clazzName.startsWith("javax.") || clazzName.startsWith("android.")) {
                Log.d(TAG, "Framework or jdk class stop upward recursion");
                break;
            }

            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                Subscribe subscriberAnnotation = method.getAnnotation(Subscribe.class);
                if (subscriberAnnotation == null) {
                    continue;
                }

                //检查subscriber方法参数，只能允许一个参数
                Class[] paratems = method.getParameterTypes();
                if (paratems.length != 1) {
                    throw new RuntimeException("TopEventBus subscriber 方法只能接收到一个参数");
                }

                ThreadMode threadMode = subscriberAnnotation.threadMode();
                SubscribleMethod subscribleMethod = new SubscribleMethod(method
                        , threadMode, paratems[0]);

                subscribleMethods.add(subscribleMethod);
            }
            clazz = clazz.getSuperclass();
        }
        return subscribleMethods;
    }

    /**
     * 触发消息传递
     * @param msg
     */
    public void post(final Object msg) {
        Set<Object> set = cacheMap.keySet();
        Iterator<Object> iterator = set.iterator();
        while (iterator.hasNext()) {
            final Object subscriber = iterator.next();
            List<SubscribleMethod> subscribleMethods = cacheMap.get(subscriber);
            for (final SubscribleMethod subscribleMethod : subscribleMethods) {
                if (subscribleMethod.getEventType().isAssignableFrom(msg.getClass())) {
                    switch (subscribleMethod.getThreadMode()) {
                        case Async:
                            //接收消息的方法在不同的子线程执行
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                //在主线程触发post消息
                                executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, subscriber, msg);
                                    }
                                });
                            }else {
                                //直接在调用post的线程中执行
                                invoke(subscribleMethod, subscriber, msg);
                            }
                            break;
                        case MainThread:
                            //接收消息的方法在主线程执行
                            if(Looper.myLooper() == Looper.getMainLooper()) {
                                //直接在主线程中触发post方法，直接调用subscriber方法
                                invoke(subscribleMethod, subscriber, msg);
                            }else {
                                //在非主线程中触发
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, subscriber, msg);
                                    }
                                });
                            }
                            break;
                        case PostThread:
                            //接收subscriber方法与post在同一个线程执行
                            invoke(subscribleMethod, subscriber, msg);
                            break;
                    }
                }
            }
        }
    }

    public void unregister(Object subscriber) {
        try {
            cacheMap.remove(subscriber);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用SubscribleMethod的method
     * @param subscribleMethod
     * @param subscriber
     * @param msg
     */
    private void invoke(SubscribleMethod subscribleMethod, Object subscriber, Object msg) {
        Method method = subscribleMethod.getMethod();
        try {
            method.invoke(subscriber, msg);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
