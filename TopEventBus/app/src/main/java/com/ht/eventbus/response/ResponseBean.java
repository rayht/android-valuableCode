package com.ht.eventbus.response;

public class ResponseBean {

    private Object data;

    public ResponseBean(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
