package com.ht.eventbus.response;

import android.util.Log;

import com.ht.eventbus.request.RequestBean;
import com.ht.eventbus.request.RequestParameter;
import com.ht.eventbus.util.TypeUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Author Leiht
 * Date 2018-05-25
 */
public class InstanceResponceMake extends ResponseMake {

    private static final String TAG = "InstanceResponceMake";

    private Method mMethod;

    @Override
    protected Object invokeMethod() {
        Object object = null;
        try {
            object = mMethod.invoke(null, mParameters);
            Log.i(TAG, "invokeMethod: " + object.toString());
            OBJECT_CENTER.putObject(object.getClass().getName(), object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void setMethod(RequestBean requestBean) {
        //解析参数
        RequestParameter[] requestParameters = requestBean.getRequestParameter();

        Class<?>[] parameterTypes = null;
        if (requestParameters != null && requestParameters.length > 0) {
            parameterTypes = new Class<?>[requestParameters.length];
            for (int i = 0; i < requestParameters.length; ++i) {
                parameterTypes[i] = typeCenter.getClassType(requestParameters[i].getParameterClassName());
            }
        }
        //请求方法
        String methodName = requestBean.getMethodName();
        //根据方法名，参数匹配找到对应的Method
        Method method = TypeUtils.getMethodForGettingInstance(reslutClass, methodName, parameterTypes);
        mMethod = method;
    }
}
