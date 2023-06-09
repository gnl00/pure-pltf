package com.pure.classloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * DynamicJarClassLoader
 *
 * @author gnl
 * @since 2023/5/11
 */
public class DynamicJarClassLoader extends URLClassLoader {

    public DynamicJarClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public void loadExternalJar(File externalJar) throws MalformedURLException {
        URL url = externalJar.toURI().toURL();
        addURL(url);
    }

    // TODO: unload the loaded jar
    public void unloadJar() {
        // 将要进行 unload 操作的 jar 从 plugins 目录中删除，然后重启应用生效。
        // 还有一种办法是管理一个 DynamicJarClassLoader 集合，
        // 每一个外部 JAR 都使用自己的 ClassLoader，卸载的时候将该 ClassLoader 关闭，进行垃圾回收
        // 打算选用第一种方法
    }
}
