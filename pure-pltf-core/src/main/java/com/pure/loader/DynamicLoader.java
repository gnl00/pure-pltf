package com.pure.loader;

import com.pure.base.PluginBase;
import com.pure.health.InfoHandler;
import org.springframework.util.Assert;

import java.util.ServiceLoader;

/**
 * DynamicLoader
 *
 * @author gnl
 * @since 2023/5/11
 */
public class DynamicLoader {
    private static ClassLoader classLoader;

    public void loadOne(ClassLoader cl) {
        Assert.notNull(cl, "ClassLoader must not be null");
        classLoader = cl;

        Thread.currentThread().setContextClassLoader(classLoader);
        servicesLoad(InfoHandler.class);
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
    }

    public <T> void loadByType(ClassLoader cl, Class<T> clz) {
        Assert.notNull(cl, "ClassLoader must not be null");
        classLoader = cl;

        Thread.currentThread().setContextClassLoader(classLoader);
        servicesLoad(clz);
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
    }

    public <T> void servicesLoad(Class<T> clazz) {
        ServiceLoader<T> services = ServiceLoader.load(clazz);
        for (T service : services) {
            PluginBase svc = (PluginBase) service;
            svc.load();
        }
    }

}
