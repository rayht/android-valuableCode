package com.ht.glide.recycle;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * @author Leiht
 * @date 2018/6/7
 */
public class LruBitmapPool extends LruCache<Integer, Bitmap> implements BitmapPool {

    /**
     * 负责排序，将size排序
     */
    private NavigableMap<Integer, Integer> map = new TreeMap<>();

    private final static int MAX_OVER_SIZE_MULTIPLE = 2;
    
    private boolean isRemoved = false;

    public LruBitmapPool(int maxSize) {
        super(maxSize);
    }

    /**
     * LruCache方法，返回value内存大小
     * @param key
     * @param value
     * @return
     */
    @Override
    protected int sizeOf(Integer key, Bitmap value) {
        return sizeOf(value);
    }

    private int sizeOf(Bitmap bitmap) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //在4.4以上的手机上复用Bitmap内存空间时，取此Bitmap的全部内存空间
            return bitmap.getAllocationByteCount();
        }
        //4.4以下时直接返回实际bitmap大小
        return bitmap.getByteCount();
    }

    @Override
    protected void entryRemoved(boolean evicted, Integer key, Bitmap oldValue, Bitmap newValue) {
        map.remove(key);
        if(null != oldValue && !isRemoved) {
            oldValue.recycle();
        }
    }

    @Override
    public void put(Bitmap bitmap) {
        //为false表示此Bitmap内存不能复用
        if(!bitmap.isMutable()) {
            bitmap.recycle();
            return;
        }

        //大于保存的最大大小，直接recycle
        int size = sizeOf(bitmap);
        if(size >= maxSize()) {
            bitmap.recycle();
            return;
        }

        put(size, bitmap);
        //保存到TreeMap,用于排序
        map.put(size, 0);
    }

    @Override
    public Bitmap get(int width, int height, Bitmap.Config confg) {
        //计算需要的Bitmap的大小
        int size = width * height * (confg == Bitmap.Config.ARGB_8888? 4 : 2);

        //取大于等于size的key
        Integer key = map.ceilingKey(size);

        if(null != key && key <= size * MAX_OVER_SIZE_MULTIPLE) {
            isRemoved = true;
            Bitmap bitmap = get(key);
            isRemoved = false;
            return bitmap;
        }
        return null;
    }
}
