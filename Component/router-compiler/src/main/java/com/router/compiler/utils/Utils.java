package com.router.compiler.utils;

import java.util.Collection;
import java.util.Map;

/**
 * @author Leiht
 * @date 2018/3/25
 */

public class Utils {


    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

}
