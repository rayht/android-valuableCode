package com.ht.glide.cache.disk;

import com.ht.glide.cache.Key;

import java.io.File;

/**
 * @author Leiht
 * @date 2018/6/3
 */
public interface DiskCache {

    File get(Key key);

    void put(Key key, Writer writer);

    void delete(Key key);

    void clear();

    interface Writer {
        boolean write(File file);
    }
}
