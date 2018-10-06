package com.ht.glide.load.model;

import android.content.ContentResolver;
import android.net.Uri;

import com.ht.glide.load.ObjectKey;
import com.ht.glide.load.model.data.FileUriFetcher;

import java.io.InputStream;

public class FileUriLoader implements ModelLoader<Uri, InputStream> {

    private final ContentResolver contentResolver;

    public FileUriLoader(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    /**
     * 构建LoaderData，即构建DataFetcher
     * 用于加载数据
     * @param uri
     * @return
     */
    @Override
    public LoadData<InputStream> buildLoadData(Uri uri) {
        return new LoadData<>(new ObjectKey(uri), new FileUriFetcher(uri, contentResolver));
    }

    /**
     * 返回Model是否匹配
     * @param uri
     * @return
     */
    @Override
    public boolean handles(Uri uri) {
        return ContentResolver.SCHEME_FILE.equalsIgnoreCase(uri.getScheme());
    }

    public static class Factory implements ModelLoaderFactory<Uri, InputStream> {

        private final ContentResolver contentResolver;

        public Factory(ContentResolver contentResolver) {
            this.contentResolver = contentResolver;
        }

        @Override
        public ModelLoader<Uri, InputStream> build(ModelLoaderRegistry modelLoaderRegistry) {
            return new FileUriLoader(contentResolver);
        }
    }

}
