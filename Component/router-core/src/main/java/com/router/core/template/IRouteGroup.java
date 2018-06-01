package com.router.core.template;

import com.router.annotation.RouteMeta;

import java.util.Map;

/**
 * @author Leiht
 * @date 2018/2/25
 */

public interface IRouteGroup {

    void loadInto(Map<String, RouteMeta> atlas);
}
