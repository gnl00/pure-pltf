package com.pure.loader;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;

/**
 * DynamicClassLoader
 *
 * @author gnl
 * @date 2023/5/11
 */
public class DynamicClassLoader {

    private final InnerReference<InnerClassLoader> innerReference;

    public DynamicClassLoader() {
        innerReference = new InnerReference<>(new InnerClassLoader(new URL[]{}));
    }

    public void show() {
        System.out.println(innerReference.get());
        release();
        System.out.println("=== after release ===");
        System.out.println(innerReference.get());
    }

    public ClassLoader instance() {
        return Objects.requireNonNull(innerReference.get());
    }

    public void release() {
        innerReference.clear();
    }

    public void add(URL url) {
        Objects.requireNonNull(innerReference.get()).add(url);
    }

    static class InnerClassLoader extends URLClassLoader {
        public InnerClassLoader(URL[] urls) {
            super(urls);
        }

        void add(URL url) {
            addURL(url);
        }
    }

    static class InnerReference<T> extends WeakReference<T> {
        public InnerReference(T referent) {
            super(referent);
        }
    }
}
