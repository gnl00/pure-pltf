package com.pure.beans;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "pltf")
public class PltfProperties {
     private static final String DEFAULT_PLUGIN_DIR = "./plugins";
     private String pluginDir = DEFAULT_PLUGIN_DIR;
}
