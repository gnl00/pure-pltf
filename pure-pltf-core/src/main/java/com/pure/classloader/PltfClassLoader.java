package com.pure.classloader;

import lombok.Setter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;

@Setter
public class PltfClassLoader extends URLClassLoader {

    private static final String CLASSES_FILE = "/target/classes/";

    private String appName;

    public PltfClassLoader(URL[] urls) {
        super(urls);
    }

    public PltfClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        String path = name.replace('.', '/').concat(".class");
        Path realPath = Paths.get(this.appName, CLASSES_FILE, path);
        synchronized (getClassLoadingLock(name)) {
            Class<?> loadedClass = null;
            File classFile = new File(realPath.toString());
            if (classFile.exists()) {
                try (InputStream fis = new FileInputStream(classFile)) {
                    byte[] content = fis.readAllBytes();
                    loadedClass = defineClass(name, content, 0, content.length);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return loadedClass;
            } else {
                return super.loadClass(name, resolve);
            }
        }
    }
}
