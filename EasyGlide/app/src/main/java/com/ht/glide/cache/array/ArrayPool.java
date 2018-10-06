package com.ht.glide.cache.array;

/**
 * @author Leiht
 * @date 2018/6/3
 */
public interface ArrayPool {

    byte[] get(int len);

    void put(byte[] data);


    void clearMemory();

    void trimMemory(int level);

    int getMaxSize();
}
