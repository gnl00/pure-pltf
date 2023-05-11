package com.pure.classloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * SpiClassLoader
 *
 * @author gnl
 * @since 2023/5/11
 */
public class SPIClassLoader extends URLClassLoader {

    public SPIClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public void loadExternalJar(File externalJar) throws MalformedURLException {
        URL url = externalJar.toURI().toURL();
        addURL(url);
    }

    // TODO: unload the loaded jar
    public void unloadJar() {}
}
