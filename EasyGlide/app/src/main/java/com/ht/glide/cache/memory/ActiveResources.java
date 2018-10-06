package com.ht.glide.cache.memory;

import com.ht.glide.cache.Key;
import com.ht.glide.recycle.Resource;
import com.ht.glide.recycle.Resource.ResourceListener;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Leiht
 * @date 2018/6/3
 */
public class ActiveResources {

    /**
     * 引用队列，初始化WeakReference时传入，在WeakReference被释放时回调
     */
    private ReferenceQueue<Resource> queue;

    /**
     * Resource移除时回调
     */
    private final ResourceListener resourceListener;

    /**
     * 缓存Map
     */
    private Map<Key, ResourceWeakReference> activeResources = new HashMap<>();

    /**
     * 线程循环，当WeakReference被回收时在线程中得到通知
     */
    private Thread cleanReferenceQueueThread;

    /**
     * 是否结束
     */
    private boolean isShutdown;

    public ActiveResources(ResourceListener resourceListener) {
        this.resourceListener = resourceListener;
    }

    /**
     * 加入活动缓存
     * @param key
     * @param resource
     */
    public void activate(Key key, Resource resource) {
        resource.setResourceListener(key, resourceListener);
        activeResources.put(key, new ResourceWeakReference(resource, getReferenceQueue(), key));
    }

    /**
     * 移除活动缓存
     * @param key
     * @return
     */
    public Resource deActive(Key key) {
        ResourceWeakReference reference = activeResources.remove(key);
        if(null != null) {
            return reference.get();
        }
        return null;
    }

    /**
     * 从活动缓存中获取Resource
     * @param key
     * @return
     */
    public Resource get(Key key) {
        ResourceWeakReference reference = activeResources.get(key);
        if(null != reference) {
            return reference.get();
        }
        return null;
    }

    public ReferenceQueue<Resource> getReferenceQueue() {
        if(null == queue) {
            queue = new ReferenceQueue<>();

            cleanReferenceQueueThread = new Thread(){
                @Override
                public void run() {
                    while (!isShutdown){
                        try {
                            ResourceWeakReference ref = (ResourceWeakReference) queue.remove();
                            activeResources.remove(ref.getKey());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            cleanReferenceQueueThread.start();
        }
        return queue;
    }

    public void shutdown() {
        isShutdown = true;
        //必须要结束cleanReferenceQueueThread，否则会导致内存泄漏
        if(null != cleanReferenceQueueThread) {
            cleanReferenceQueueThread.interrupt();
            try {
                cleanReferenceQueueThread.join(TimeUnit.SECONDS.toMillis(5));

                if (cleanReferenceQueueThread.isAlive()) {
                    throw new RuntimeException("Failed to join in time");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static final class ResourceWeakReference extends WeakReference<Resource> {

        private Key key;

        public ResourceWeakReference(Resource referent, ReferenceQueue<? super Resource> q, Key key) {
            super(referent, q);
            this.key = key;
        }

        public Key getKey() {
            return key;
        }
    }
}
