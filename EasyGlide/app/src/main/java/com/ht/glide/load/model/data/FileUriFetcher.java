package com.ht.glide.load.model.data;

import android.content.ContentResolver;
import android.net.Uri;

import java.io.InputStream;

/**
 * @author Leiht
 * @date 2018/6/3
 */
public class FileUriFetcher implements DataFetcher<InputStream> {
    public FileUriFetcher(Uri uri, ContentResolver contentResolver) {
    }

    /**
     * 加载数据（从网络或者File）
     *
     * @param callback
     */
    @Override
    public void loadData(DataFetcherCallback<? super InputStream> callback) {

    }

    /**
     * 取消请求
     */
    @Override
    public void cancel() {

    }

    /**
     * 返回Class
     *
     * @return
     */
    @Override
    public Class<InputStream> getDataClass() {
        return null;
    }
}
