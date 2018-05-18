package com.ht.easy.net;

import com.ht.easy.net.chain.Interceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leiht
 * @date 2018/5/3
 */
public class HttpClient {

    private Dispatcher dispatcher;

    /**
     * 重试次数
     */
    private int retyrs;

    private final ConnectionPool connectionPool;

    private List<Interceptor> interceptors;

    public HttpClient(Builder builder) {
        this.dispatcher = builder.dispatcher;
        this.retyrs = builder.retrys;
        this.connectionPool = builder.connectionPool;
        this.interceptors = builder.interceptors;
    }

    public Dispatcher dispatcher() {
        return dispatcher;
    }

    public int retyrs() {
        return retyrs;
    }

    public ConnectionPool connectionPool() {
        return connectionPool;
    }

    public List<Interceptor> interceptors() {
        return interceptors;
    }

    public Call newCall(Request request) {
        return new Call(this, request);
    }

    public static final class Builder {
        Dispatcher dispatcher;

        int retrys;

        ConnectionPool connectionPool;

        List<Interceptor> interceptors;

        public Builder dispatcher(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
            return this;
        }

        public Builder retrys(int retrys) {
            this.retrys = retrys;
            return this;
        }

        public Builder connectionPool(ConnectionPool connectionPool) {
            this.connectionPool = connectionPool;
            return this;
        }

        public Builder interceptor(Interceptor interceptor) {
            if(interceptors == null) {
                interceptors = new ArrayList<>();
            }
            interceptors.add(interceptor);
            return this;
        }

        public HttpClient build() {
            if(dispatcher == null) {
                dispatcher = new Dispatcher();
            }

            if(connectionPool == null) {
                connectionPool = new ConnectionPool();
            }

            return new HttpClient(this);
        }
    }

}
