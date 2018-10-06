package com.ht.glide.load.model;

import com.ht.glide.cache.Key;
import com.ht.glide.load.model.data.DataFetcher;

/**
 * @author Leiht
 * @date 2018/6/9
 * <p>
 * <p>
 * <Model, Data>类型如下
 * HttpModelLoader: Model->Uri.class, Data->InputStream.class
 * UriFileModelLoader:Model->Uri.class, Data->InputStream.class
 * FileModelLoader:Model->File.class, Data->InputStream.class
 * StringModelLoader:Model->String.class, Data->InputStream.class
 * </p>
 */
public interface ModelLoader<Model, Data> {

    /**
     * 构建LoaderData，即构建DataFetcher
     * 用于加载数据
     * @param model
     * @return
     */
    LoadData<Data> buildLoadData(Model model);

    /**
     * 返回Model是否匹配
     * @param model
     * @return
     */
    boolean handles(Model model);

    interface ModelLoaderFactory<Model, Data> {
        ModelLoader<Model, Data> build(ModelLoaderRegistry modelLoaderRegistry);
    }

    class LoadData<Data> {
        public final Key sourceKey;

        public final DataFetcher fetcher;

        public LoadData(Key sourceKey, DataFetcher fetcher) {
            this.sourceKey = sourceKey;
            this.fetcher = fetcher;
        }
    }

}
