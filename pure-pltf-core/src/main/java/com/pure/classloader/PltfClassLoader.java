package com.pure.classloader;

import java.net.URL;
import java.net.URLClassLoader;

public class PltfClassLoader extends URLClassLoader {
    public PltfClassLoader(URL[] urls) {
        super(urls);
    }

    public PltfClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
}
