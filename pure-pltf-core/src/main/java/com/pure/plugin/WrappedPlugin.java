package com.pure.plugin;

import com.pure.Plugin;
import com.pure.classloader.DynamicClassLoader;
import lombok.*;

import java.util.Objects;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WrappedPlugin {

    private Plugin plugin;
    private Boolean isEnable = false;
    private DynamicClassLoader pluginLoader;

    public ClassLoader getPluginLoader() {
        return Objects.nonNull(pluginLoader)? pluginLoader.instance() : null;
    }


    public void release() {
        if (Objects.nonNull(pluginLoader)) {
            pluginLoader.release();
        }
    }
}
