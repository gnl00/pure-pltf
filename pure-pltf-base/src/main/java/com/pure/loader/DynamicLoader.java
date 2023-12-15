package com.pure.loader;

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
}
