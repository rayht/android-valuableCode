package com.ht.glide.cache;

import java.security.MessageDigest;

public interface Key {

    void updateDiskCacheKey(MessageDigest messageDigest);

    byte[] getKeyBytes();

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

}
