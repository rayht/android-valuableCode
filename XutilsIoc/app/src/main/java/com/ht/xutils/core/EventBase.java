package com.ht.xutils.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface EventBase {

    /**
     * 设置listener的方法-setOnClickListener,setOnLongClickListener
     * @return
     */
    String listenerSetter();

    /**
     * listener事件类型
     * OnClickListener,OnLongClickListener
     * @return
     */
    Class<?> listenerType();

    /**
     * Listener事件中回调的方法名
     * @return
     */
    String callbackMethod();

}
