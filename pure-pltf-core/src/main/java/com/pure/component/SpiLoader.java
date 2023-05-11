package com.pure.component;

import com.pure.base.SpiBase;
import com.pure.spi.InfoSpi;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * SpiLoader
 *
 * @author gnl
 * @since 2023/5/11
 */
@Component
public class SpiLoader {
    private static ClassLoader classLoader;

    public void load(ClassLoader cl) {
        Assert.notNull(cl, "SPI ClassLoader must not be null");
        classLoader = cl;

        Thread.currentThread().setContextClassLoader(classLoader);
        servicesLoad(SpiLoader.class);
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
    }

    public <T> void load(ClassLoader cl, Class<T> clz) {
        Assert.notNull(cl, "SPI ClassLoader must not be null");
        classLoader = cl;

        Thread.currentThread().setContextClassLoader(classLoader);
        servicesLoad(clz);
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
    }

    public <T> void servicesLoad(Class<T> clazz) {
        ServiceLoader<T> services = ServiceLoader.load(clazz);
        for (T service : services) {
            SpiBase svc = (SpiBase) service;
            svc.load();
        }
    }

}
