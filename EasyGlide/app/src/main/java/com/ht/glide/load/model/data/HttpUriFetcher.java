package com.ht.glide.load.model.data;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Leiht
 * @date 2018/6/10
 */
public class HttpUriFetcher implements DataFetcher<InputStream> {

    private final Uri uri;
    private boolean isCanceled;

    public HttpUriFetcher(Uri uri) {
        this.uri = uri;
    }

    /**
     * 加载数据（从网络或者File）
     * @param callback
     */
    @Override
    public void loadData(DataFetcherCallback<? super InputStream> callback) {
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            URL url = new URL(uri.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            is = conn.getInputStream();
            int responseCode = conn.getResponseCode();
            if (isCanceled) {
                return;
            }
            if (responseCode == HttpURLConnection.HTTP_OK) {
                callback.onFetcherReady(is);
            } else {
                callback.onLoadFailed(new RuntimeException(conn.getResponseMessage()));
            }
        } catch (Exception e) {
            callback.onLoadFailed(e);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != conn) {
                conn.disconnect();
            }
        }
    }

    /**
     * 取消请求
     */
    @Override
    public void cancel() {
        this.isCanceled = true;
    }

    /**
     * 返回Class
     * @return
     */
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }
}
