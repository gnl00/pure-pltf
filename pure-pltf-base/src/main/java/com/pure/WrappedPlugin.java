package com.pure;

import com.pure.loader.DynamicClassLoader;
import lombok.*;

import java.util.Objects;

@Data
@Builder
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
