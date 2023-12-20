package com.pure.loader;

import java.util.ServiceLoader;

/**
 * DynamicLoader
 *
 * @author gnl
 * @date 2023/5/11
 */
public class DynamicLoader {

    public <T> T loadOne(ClassLoader cl, Class<T> clazz) {
        Thread.currentThread().setContextClassLoader(cl);
        ServiceLoader<T> services = doLoad(clazz);
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        // 强制要求所有的插件都只能有一个 Plugin 的实现类
        // 所以只需要处理第一个 plugin
        return services.findFirst().isPresent() ? services.findFirst().get() : null;
    }

    public <T> ServiceLoader<T> load(ClassLoader cl, Class<T> clazz) {
        Thread.currentThread().setContextClassLoader(cl);
        ServiceLoader<T> services = doLoad(clazz);
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        return services;
    }

    private <T> ServiceLoader<T> doLoad(Class<T> clazz) {
        // ServiceLoader.load 的时候会执行 Plugin 无参构造方法
        return ServiceLoader.load(clazz);
    }
}
