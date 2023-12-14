package com.pure.global;

import com.pure.base.Plugin;
import com.pure.loader.DynamicClassLoader;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * GlobalRef
 *
 * @author gnl
 * @since 2023/5/11
 */
public class GlobalRef {
    public static DynamicClassLoader pluginClassLoader;

    public static Map<String, Plugin> plugins = new HashMap<>(); // cache plugins

    public static void setDynamicClassloader(DynamicClassLoader classloader) {
        Assert.notNull(classloader, "Classloader cannot be null");
        pluginClassLoader = classloader;
    }
}
