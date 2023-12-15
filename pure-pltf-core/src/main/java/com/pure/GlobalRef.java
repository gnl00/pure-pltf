package com.pure;

import com.pure.base.Plugin;
import com.pure.loader.TestClassLoader;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * GlobalRef
 *
 * @author gnl
 * @date 2023/5/11
 */
public class GlobalRef {
    public static TestClassLoader pluginClassLoader;

    public static Map<String, Plugin> plugins = new HashMap<>(); // cache plugins

    public static void setDynamicClassloader(TestClassLoader classloader) {
        Assert.notNull(classloader, "Classloader cannot be null");
        pluginClassLoader = classloader;
    }
}
