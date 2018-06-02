package com.ht.eventbus.request;

/**
 * Author Leiht
 * Date 2018-05-25
 */
public class RequestBean {

    /**
     * 请求对象的Class Name
     */
    private String className;

    /**
     * 返回类型的Class Name
     */
    private String resultClassName;

    /**
     *
     */
    private String requestObject;

    /**
     * 请求方法名，getInstance默认为null
     */
    private String methodName;

    /**
     * 请求方法methodName对应的请求参数数组
     */
    private RequestParameter[] requestParameter;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getResultClassName() {
        return resultClassName;
    }

    public void setResultClassName(String resultClassName) {
        this.resultClassName = resultClassName;
    }

    public String getRequestObject() {
        return requestObject;
    }

    public void setRequestObject(String requestObject) {
        this.requestObject = requestObject;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public RequestParameter[] getRequestParameter() {
        return requestParameter;
    }

    public void setRequestParameter(RequestParameter[] requestParameter) {
        this.requestParameter = requestParameter;
    }
}
