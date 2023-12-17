package com.pure;

import com.pure.loader.TestClassLoader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ServiceLoader;

public class Test {
    public static void main(String[] args) throws IOException {
        // list local jars
        Files.list(Paths.get("./"))
                .filter(file -> file.getFileName().toString().lastIndexOf(".jar") != -1)
                .forEach(file -> {
                    File jar = new File(file.toUri());
                    try {
                        URL url = jar.toURI().toURL();
                        TestClassLoader classLoader = new TestClassLoader(new URL[]{url});
                        for (Plugin plugin : ServiceLoader.load(Plugin.class, classLoader)) {
                            System.out.println(plugin);
                            plugin.exec();
                        }

                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
