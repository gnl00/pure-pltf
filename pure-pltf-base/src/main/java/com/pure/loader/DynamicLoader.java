package com.pure.loader;

import java.util.ServiceLoader;

/**
 * DynamicLoader
 *
 * @author gnl
 * @date 2023/5/11
 */
public class DynamicLoader {

    public void load(ClassLoader cl) {
        Thread.currentThread().setContextClassLoader(cl);
        // TODO do something
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
    }

    public <T> ServiceLoader<T> load(ClassLoader cl, Class<T> clazz) {
        Thread.currentThread().setContextClassLoader(cl);
        ServiceLoader<T> services = doLoad(clazz);
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        return services;
    }

    private <T> ServiceLoader<T> doLoad(Class<T> clazz) {
        // ServiceLoader.load 的时候会执行 Plugin 无参构造方法
        ServiceLoader<T> services = ServiceLoader.load(clazz);
        return services;
    }
}
