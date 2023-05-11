package com.pure.global;

import com.pure.classloader.SPIClassLoader;
import org.springframework.util.Assert;

/**
 * GlobalRef
 *
 * @author gnl
 * @since 2023/5/11
 */
public class GlobalRef {
    public static SPIClassLoader pluginClassLoader;

    public static void setGlobalClassloader(SPIClassLoader classloader) {
        Assert.notNull(classloader, "Classloader cannot be null");
        pluginClassLoader = classloader;
    }
}
