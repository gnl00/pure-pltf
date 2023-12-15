package com.pure.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pure.loader.DynamicClassLoader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Plugin metadata
 *
 * @author gnl
 * @date 2023/5/12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginMetadata {
    private String name;
    private String description;
    private String version;
    private Boolean isEnable;
    @JsonIgnore
    private DynamicClassLoader classLoader;

    public ClassLoader getClassLoaderInstance() {
        return classLoader.instance();
    }
}