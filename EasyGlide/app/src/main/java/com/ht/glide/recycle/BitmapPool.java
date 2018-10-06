package com.ht.glide.recycle;

import android.graphics.Bitmap;

/**
 * @author Leiht
 * @date 2018/6/7
 */
public interface BitmapPool {

    void put(Bitmap bitmap);

    Bitmap get(int width, int height, Bitmap.Config confg);

}
