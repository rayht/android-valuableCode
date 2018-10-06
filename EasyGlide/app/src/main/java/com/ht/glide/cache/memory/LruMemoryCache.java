package com.ht.glide.cache.memory;

import android.os.Build;
import android.util.LruCache;

import com.ht.glide.cache.Key;
import com.ht.glide.recycle.Resource;

/**
 * @author Leiht
 * @date 2018/6/3
 */
public class LruMemoryCache extends LruCache<Key, Resource> implements MemoryCache {

    /**
     * 标记移除是否是手动调用，如是则不回调ResourceRemovedListener
     */
    private boolean isManualRemove = false;

    /**
     * ResourceRemovedListener,被移除时回调
     */
    private ResourceRemovedListener resourceRemovedListener;

    public LruMemoryCache(int maxSize) {
        super(maxSize);
    }

    /**
     * 计算返回value占用的内存空间
     * 这里即Resource里的Bitmap占用内存大小
     * @param key
     * @param value
     * @return
     */
    @Override
    protected int sizeOf(Key key, Resource value) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //在4.4以上的手机上复用Bitmap内存空间时，取此Bitmap的全部内存空间
            return value.getBitmap().getAllocationByteCount();
        }
        //4.4以下时直接返回实际bitmap大小
        return value.getBitmap().getByteCount();
    }

    /**
     * 主动移除，即用户主动调用该方法移除缓存
     * LruCache中的remove(Key key)方法用户Lru算法在put时删除标记为不活跃的缓存节点
     * @param key
     * @return
     */
    @Override
    public Resource remove2(Key key) {
        //主动remove,标记不回调ResourceRemovedListener
        isManualRemove = true;
        Resource remove = remove(key);
        isManualRemove = false;
        return remove;
    }

    /**
     * LruCache中移除节点时回调，oldValue为被移除的节点对象
     *
     * @param evicted
     * @param key
     * @param oldValue
     * @param newValue
     */
    @Override
    protected void entryRemoved(boolean evicted, Key key, Resource oldValue, Resource newValue) {
        //LRU算法标记被移除时回调ResourceRemovedListener
        if(resourceRemovedListener != null && oldValue != null && !isManualRemove) {
            resourceRemovedListener.onResourceRemoved(oldValue);
        }
    }

    @Override
    public void setResourceRemovedListener(ResourceRemovedListener resourceRemovedListener) {
        this.resourceRemovedListener = resourceRemovedListener;
    }

    @Override
    public void clearMemory() {
        evictAll();
    }

    //TODO this method
    @Override
    public void trimMemory(int level) {
        if (level >= android.content.ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            clearMemory();
        } else if (level >= android.content.ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            trimToSize(maxSize() / 2);
        }
    }
}
