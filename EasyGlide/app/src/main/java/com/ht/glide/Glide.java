package com.ht.glide;

import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.ht.glide.cache.array.ArrayPool;
import com.ht.glide.cache.disk.DiskCache;
import com.ht.glide.cache.memory.MemoryCache;
import com.ht.glide.load.codec.StreamBitmapDecoder;
import com.ht.glide.load.model.FileUriLoader;
import com.ht.glide.load.model.HttpUriLoader;
import com.ht.glide.load.model.StringModelLoader;
import com.ht.glide.recycle.BitmapPool;
import com.ht.glide.request.RequestOptions;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.ThreadPoolExecutor;

public class Glide implements ComponentCallbacks2 {
    private static volatile Glide glide;

    private BitmapPool bitmapPool;
    private MemoryCache memoryCache;
    private DiskCache diskCache;
    private Context context;
    private ThreadPoolExecutor executor;
    private Registry registry;
    private ArrayPool arrayPool;

    Glide(Context context, GlideBuilder
            glideBuilder) {
        this.context = context.getApplicationContext();
        //注册机
        registry = new Registry();
        ContentResolver contentResolver = context.getContentResolver();
        registry.add(String.class, InputStream.class, new StringModelLoader.Factory())
                .add(Uri.class, InputStream.class, new HttpUriLoader.Factory())
                .add(Uri.class, InputStream.class, new FileUriLoader.Factory(contentResolver))
                .register(InputStream.class, new StreamBitmapDecoder(bitmapPool, arrayPool));
    }

    public Registry getRegistry() {
        return registry;
    }
}

