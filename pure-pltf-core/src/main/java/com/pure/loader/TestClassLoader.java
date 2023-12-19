package com.pure.loader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * TestClassLoader
 *
 * @author gnl
 * @date 2023/5/11
 */
public class TestClassLoader extends URLClassLoader {

    public TestClassLoader(URL[] urls) {
        super(urls);
    }

    public void load(URL url) {
        addURL(url);
    }

    public void unload() { // TODO
    }
}
