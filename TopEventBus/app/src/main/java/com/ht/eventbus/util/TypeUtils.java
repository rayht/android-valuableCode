package com.ht.eventbus.util;

import android.app.Activity;
import android.app.Application;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.Method;
import java.util.HashSet;

public class TypeUtils {
    private static final HashSet<Class<?>> CONTEXT_CLASSES = new HashSet<Class<?>>() {
        {
            add(Context.class);
            add(Activity.class);
            add(AppCompatActivity.class);
            add(Application.class);
            add(FragmentActivity.class);
            add(IntentService.class);
            add(Service.class);
        }
    };

    public static String getMethodId(Method method) {
        StringBuilder result = new StringBuilder(method.getName());
        result.append('(').append(getMethodParameters(method.getParameterTypes())).append(')');
        return result.toString();
    }

    public static String getMethodParameters(Class<?>[] classes) {
        StringBuilder result = new StringBuilder();
        int length = classes.length;
        if (length == 0) {
            return result.toString();
        }
        result.append(getClassName(classes[0]));
        for (int i = 1; i < length; ++i) {
            result.append(",").append(getClassName(classes[i]));
        }
        return result.toString();
    }

    private static String getClassName(Class<?> clazz) {
        if (clazz == Boolean.class) {
            return "boolean";
        } else if (clazz == Byte.class) {
            return "byte";
        } else if (clazz == Character.class) {
            return "char";
        } else if (clazz == Short.class) {
            return "short";
        } else if (clazz == Integer.class) {
            return "int";
        } else if (clazz == Long.class) {
            return "long";
        } else if (clazz == Float.class) {
            return "float";
        } else if (clazz == Double.class) {
            return "double";
        } else if (clazz == Void.class) {
            return "void";
        } else {
            return clazz.getName();
        }
    }
}
