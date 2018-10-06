package com.ht.glide.load.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leiht
 * @date 2018/6/9
 * ModelLoader登记表
 */
public class ModelLoaderRegistry {

    /**
     * 保存注册的ModelLoader集合
     */
    private List<Entry<?, ?>> entries = new ArrayList<>();

    /**
     * 初始化注册（保存）已实现的ModelLoader
     * @param modelClass
     * @param dataClass
     * @param modelLoaderFactory
     * @param <Model>
     * @param <Data>
     */
    public synchronized <Model, Data> void add(Class<Model> modelClass,
                                               Class<Data> dataClass,
                                               ModelLoader.ModelLoaderFactory<? extends Model, ? extends Data> modelLoaderFactory) {
        Entry<Model, Data> entry = new Entry<>(modelClass, dataClass, modelLoaderFactory);
        entries.add(entry);
    }

    /**
     * 根据modelClass，dataClass从entries中匹配后创建对应的ModelLoader对象
     * @param modelClass
     * @param dataClass
     * @param <Model>
     * @param <Data>
     * @return
     */
    public synchronized <Model, Data> ModelLoader<Model, Data> build(Class<Model> modelClass,
                                                                     Class<Data> dataClass) {
        List<ModelLoader<Model, Data>> loaders = new ArrayList<>();
        for (Entry<?, ?> entry : entries) {
            if (entry.handles(modelClass, dataClass)) {
                loaders.add((ModelLoader<Model, Data>) entry.factory.build(this));
            }
        }
        if (loaders.size() > 1) {
            return new MultiModelLoader<>(loaders);
        } else if (loaders.size() == 1) {
            return loaders.get(0);
        }
        throw new RuntimeException("No Have:" + modelClass.getName() + " Model Match " +
                dataClass.getName() + " Data");
    }


    /**
     * 获得符合model类型的loader集合
     * @param modelClass
     * @param <Model>
     * @return
     */
    public <Model> List<ModelLoader<Model, ?>> getModelLoaders(Class<Model> modelClass) {
        List<ModelLoader<Model, ?>> modelLoaders = new ArrayList<>();
        for (Entry<?, ?> entry : entries) {
            //model 符合的加入集合
            if (entry.handles(modelClass)) {
                modelLoaders.add((ModelLoader<Model, ?>) entry.factory.build(this));
            }
        }
        return modelLoaders;
    }

    /**
     * ModelLoader注册保存实体类
     * 用于存储Model.class,Data.class及对应的Factory
     * @param <Model>
     * @param <Data>
     */
    private static class Entry<Model, Data> {
        private final Class<Model> modelClass;
        final Class<Data> dataClass;
        final ModelLoader.ModelLoaderFactory<? extends Model, ? extends Data> factory;

        public Entry(
                Class<Model> modelClass,
                Class<Data> dataClass,
                ModelLoader.ModelLoaderFactory<? extends Model, ? extends Data> factory) {
            this.modelClass = modelClass;
            this.dataClass = dataClass;
            this.factory = factory;
        }

        public boolean handles(@NonNull Class<?> modelClass, @NonNull Class<?> dataClass) {
            return handles(modelClass) && this.dataClass.isAssignableFrom(dataClass);
        }

        private boolean handles(@NonNull Class<?> modelClass) {
            return this.modelClass.isAssignableFrom(modelClass);
        }
    }
}
