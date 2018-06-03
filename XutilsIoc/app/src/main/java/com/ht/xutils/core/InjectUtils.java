package com.ht.xutils.core;

import android.mtp.MtpConstants;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectUtils {

    /**
     * 注入接口方法
     * @param activity
     */
    public static void inject(Object activity) {
        injectLayout(activity);
        injectView(activity);
        injectEvent(activity);
    }

    /**
     * 注入事件(OnClick,OnLongClick...)
     * @param activity
     */
    private static void injectEvent(Object activity) {
        Class<?> clazz = activity.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        Method findViewByIdMethod = null;
        if(methods != null && methods.length > 0) {
            for(Method method : methods) {
                Annotation[] annotations = method.getAnnotations();
                if(annotations != null && annotations.length > 0) {
                    for(Annotation annotation : annotations) {
                        Class<?> annotationType = annotation.annotationType();
                        EventBase eventBase = annotationType.getAnnotation(EventBase.class);
                        if(eventBase == null) {
                            continue;
                        }

                        //事件三要素之setter方法-setOnClickListener
                        String listenerSetter = eventBase.listenerSetter();

                        //事件三要素之事件类型-OnClickListener
                        Class<?> listenerType = eventBase.listenerType();

                        //事件三要素之事件类型-onClick
                        String callbackMethod = eventBase.callbackMethod();

                        Method valueMethod = null;

                        try {
                            valueMethod = annotationType.getDeclaredMethod("value");

                            int[] valueIds = (int[]) valueMethod.invoke(annotation);

                            if(valueIds != null && valueIds.length > 0) {
                                for(int id : valueIds) {
                                    if(null == findViewByIdMethod) {
                                        findViewByIdMethod = clazz.getMethod("findViewById", int.class);
                                    }
                                    View view = (View) findViewByIdMethod.invoke(activity, id);
                                    if(null == view) {
                                        continue;
                                    }

                                    Method setterMethod = view.getClass().getMethod(listenerSetter, listenerType);

                                    ListenerInvocationHandler listenerInvocationHandler = new ListenerInvocationHandler(activity, method);

                                    Object proxy = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class<?>[]{listenerType}, listenerInvocationHandler);

                                    setterMethod.invoke(view, proxy);
                                }
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 注入View
     * @param activity
     */
    private static void injectView(Object activity) {
        try {
            Class<?> clazz = activity.getClass();
            Field[] fields = clazz.getDeclaredFields();
            Method method = null;
            for(Field field : fields) {
                ViewInject viewInject = field.getAnnotation(ViewInject.class);
                if(null == viewInject) {
                    continue;
                }
                int viewId = viewInject.value();
                field.setAccessible(true);
                if(null == method) {
                    method = clazz.getMethod("findViewById", int.class);
                }
                View view = (View) method.invoke(activity, viewId);
                field.set(activity, view);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注入Layout
     * @param activity
     */
    private static void injectLayout(Object activity) {
        Class<?> clazz = activity.getClass();
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null) {
            int layoutId = contentView.value();
            try {
                Method method = clazz.getMethod("setContentView", int.class);
                method.setAccessible(true);
                method.invoke(activity, layoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
