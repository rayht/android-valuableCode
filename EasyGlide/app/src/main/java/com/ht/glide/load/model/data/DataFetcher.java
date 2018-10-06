package com.ht.glide.load.model.data;

/**
 * @author Leiht
 * @date 2018/6/3
 *  数据获取器
 */
public interface DataFetcher<T> {

    /**
     * 加载数据（从网络或者File）
     * @param callback
     */
    void loadData(DataFetcherCallback<? super T> callback);

    /**
     * 取消请求
     */
    void cancel();

    /**
     * 返回Class
     * @return
     */
    Class<T> getDataClass();

    /**
     * 数据获取后的回调，即正常返回InputStream或产生异常后回调
     * @param <T>
     */
    interface DataFetcherCallback<T> {

        void onFetcherReady(T data);

        void onLoadFailed(Exception e);
    }

}

