package com.ht.glide.load.model;

import android.net.Uri;

import com.ht.glide.load.ObjectKey;
import com.ht.glide.load.model.data.HttpUriFetcher;

import java.io.InputStream;

/**
 * @author Leiht
 * @date 2018/6/9
 */
public class HttpUriLoader implements ModelLoader<Uri, InputStream> {
    /**
     * 构建LoaderData，即构建DataFetcher
     * 用于加载数据
     *
     * @param uri
     * @return
     */
    @Override
    public LoadData<InputStream> buildLoadData(Uri uri) {
        return new LoadData<InputStream>(new ObjectKey(uri), new HttpUriFetcher(uri));
    }

    /**
     * 返回Model是否匹配
     *
     * @param uri
     * @return
     */
    @Override
    public boolean handles(Uri uri) {
        String scheme = uri.getScheme();
        return scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("HTTPS");
    }

    public static class Factory implements ModelLoaderFactory<Uri, InputStream> {
        @Override
        public ModelLoader<Uri, InputStream> build(ModelLoaderRegistry modelLoaderRegistry) {
            return new HttpUriLoader();
        }
    }
}
