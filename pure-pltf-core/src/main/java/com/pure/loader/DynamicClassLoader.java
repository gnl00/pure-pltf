package com.pure.loader;

import com.pure.Plugin;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@Getter
@Setter
@ToString
public class DynamicClassLoader {

    private Plugin plugin; // load for which plugin? this filed will tell you
    @ToString.Exclude
    private final InnerReference<InnerClassLoader> innerReference;

    public DynamicClassLoader(URL url) {
        innerReference = new InnerReference<>(new InnerClassLoader(new URL[]{}));
        add(url);
    }

    public void show() {
        System.out.println(innerReference.get());
        release();
        System.out.println("=== after release ===");
        System.out.println(innerReference.get());
    }

    public ClassLoader instance() {
        return innerReference.get();
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
