package com.ht.eventbus.response;

import com.google.gson.Gson;
import com.ht.eventbus.Request;
import com.ht.eventbus.Response;
import com.ht.eventbus.core.ObjectCenter;
import com.ht.eventbus.core.TypeCenter;
import com.ht.eventbus.request.RequestBean;
import com.ht.eventbus.request.RequestParameter;

/**
 * Author Leiht
 * Date 2018-05-25
 */
public abstract class ResponseMake {

    protected Class<?> reslutClass;
    // getInstance()  参数数组
    protected Object[] mParameters;

    Gson GSON = new Gson();

    protected TypeCenter typeCenter = TypeCenter.getInstance();

    protected static final ObjectCenter OBJECT_CENTER = ObjectCenter.getInstance();


    protected abstract Object invokeMethod();

    protected abstract void setMethod(RequestBean requestBean);

    public Response makeResponse(Request request) {
        RequestBean requestBean = GSON.fromJson(request.getData(), RequestBean.class);
        reslutClass = typeCenter.getClassType(requestBean.getResultClassName());
        RequestParameter[] requestParameters = requestBean.getRequestParameter();
        if (requestParameters != null && requestParameters.length > 0) {
            mParameters = new Object[requestParameters.length];
            for (int i = 0; i < requestParameters.length; i++) {
                RequestParameter requestParameter = requestParameters[i];
                Class<?> clazz = typeCenter.getClassType(requestParameter.getParameterClassName());
                mParameters[i] = GSON.fromJson(requestParameter.getParameterValue(), clazz);
            }
        } else {
            mParameters = new Object[0];
        }

        setMethod(requestBean);
        Object resultObject = invokeMethod();
        ResponseBean responceBean = new ResponseBean(resultObject);
        String data = GSON.toJson(responceBean);
        Response responce = new Response(data);
        return responce;
    }
}
