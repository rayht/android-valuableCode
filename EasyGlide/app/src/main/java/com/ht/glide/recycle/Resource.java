package com.ht.glide.recycle;

import android.graphics.Bitmap;

import com.ht.glide.cache.Key;

/**
 * @author Leiht
 * @date 2018/6/3
 * 对Bitmap的封装，主要用于引用计数。
 */
public class Resource {

    /**
     * 实际Bitmap对象
     */
    private Bitmap bitmap;

    /**
     * 实际Bitmap对象的引用计数
     */
    private int acquired;

    /**
     * 唯一key
     */
    private Key key;

    /**
     * Listener,当acquard==0时回调onResourceReleased()
     * 将bitmap从活动资源移到内存缓存
     */
    private ResourceListener resourceListener;

    public Resource() {

    }

    public Resource(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setResourceListener(Key key, ResourceListener resourceListener) {
        this.key = key;
        this.resourceListener = resourceListener;
    }

    public ResourceListener getResourceListener() {
        return resourceListener;
    }

    /**
     * 释放Resource(Bitmap)
     */
    public void recycle() {
        if(acquired > 0) {
            return;
        }
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    /**
     * 引用计数加1
     */
    public void acquire() {
        if (bitmap.isRecycled()) {
            throw new IllegalStateException("Cannot acquire a recycled resource");
        }
        ++acquired;
    }

    /**
     * 引用计数-1，如果没有引用==0， 回调onResourceReleased
     */
    public void release() {
        if (--acquired == 0) {
            resourceListener.onResourceReleased(key, this);
        }
    }

    public interface ResourceListener {
        //回调主要用于将bitmap从活动资源移到内存缓存
        void onResourceReleased(Key key, Resource resource);
    }
}
