package com.ht.easy.net;

import android.support.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Leiht
 * @date 2018/5/3
 */
public class Dispatcher {

    //最多同时请求
    private int maxRequests = 64;
    //同一个host同时最多请求
    private int maxRequestsPerHost = 5;

    /**
     * 等待执行队列
     */
    private final Deque<Call.AsyncCall> readyAsyncCalls = new ArrayDeque<Call.AsyncCall>();

    /**
     * 正在执行队列
     */
    private final Deque<Call.AsyncCall> runningAsyncCalls = new ArrayDeque<>();

    /**
     * 线程池
     */
    private ExecutorService executorService;

    public Dispatcher() {
        this(64, 5);
    }

    public synchronized ExecutorService executorService() {
        if (null == executorService) {
            //线程工厂 创建线程
            ThreadFactory threadFactory = new ThreadFactory() {
                @Override
                public Thread newThread(@NonNull Runnable runnable) {
                    return new Thread(runnable, "Http Client");
                }
            };
            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60,
                    TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), threadFactory);
        }
        return executorService;
    }

    public Dispatcher(int maxRequests, int maxRequestsPerHost) {
        this.maxRequests = maxRequests;
        this.maxRequestsPerHost = maxRequestsPerHost;
    }

    /**
     * 使用调度器进行任务调度
     */
    public void enqueue(Call.AsyncCall call) {
        //不能超过最大请求数与相同host的请求数
        //满足条件意味着可以马上开始任务
        if (runningAsyncCalls.size() < maxRequests && runningCallsForHost(call) < maxRequestsPerHost) {
            runningAsyncCalls.add(call);
            executorService().execute(call);
        } else {
            readyAsyncCalls.add(call);
        }
    }

    /**
     * 同一个host正在请求队列里的个数
     * @param call
     * @return
     */
    private int runningCallsForHost(Call.AsyncCall call) {
        int result = 0;
        for (Call.AsyncCall c : runningAsyncCalls) {
            if (c.host().equals(call.host())) {
                result++;
            }
        }
        return result;
    }

    /**
     * 表示一个请求成功
     * 将其从runningAsync移除
     * 并且检查ready是否可以执行
     *
     * @param call
     */
    public void finished(Call.AsyncCall call) {
        synchronized (this) {
            //将其从runningAsync移除
            runningAsyncCalls.remove(call);
            //检查是否可以运行ready
            promoteCalls();
        }
    }

    /**
     * 判断是否执行等待队列中的请求
     */
    private void promoteCalls() {
        if(runningAsyncCalls.size() > maxRequests) {
            return;
        }

        //等待队列为空
        if(readyAsyncCalls.isEmpty()) {
            return;
        }

        for (Iterator<Call.AsyncCall> iterator = readyAsyncCalls.iterator(); iterator.hasNext(); ) {
            Call.AsyncCall syncCall = iterator.next();
            //同一host上请求未达最大数
            if(runningCallsForHost(syncCall) < maxRequestsPerHost) {
                iterator.remove();
                runningAsyncCalls.add(syncCall);
                executorService.execute(syncCall);
            }

            //已达到最大请求数
            if(runningAsyncCalls.size() >= maxRequests) {
                return;
            }
        }
    }
}