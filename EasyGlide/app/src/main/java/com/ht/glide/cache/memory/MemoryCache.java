package com.ht.glide.cache.memory;

import com.ht.glide.cache.Key;
import com.ht.glide.recycle.Resource;

/**
 * @author Leiht
 * @date 2018/6/3
 */
public interface MemoryCache {

    void setResourceRemovedListener(ResourceRemovedListener listener);

    Resource put(Key key, Resource resource);

    Resource remove2(Key key);

    void clearMemory();

    void trimMemory(int level);

    /**
     * Resource从内存缓存中移除时回调Listener
     */
    interface ResourceRemovedListener {
        void onResourceRemoved(Resource resource);
    }
}
