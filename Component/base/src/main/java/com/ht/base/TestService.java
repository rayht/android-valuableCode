package com.ht.base;

/**
 * @author Leiht
 * @date 2018/3/25
 * <p>
 * <p>
 * 需要组件共享的服务需要将服务在此暴露
 */
public interface TestService extends IService {
    void test();
}
