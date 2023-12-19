package com.pure;

import lombok.Data;

public abstract class Plugin implements PluginHook {

    public static final int MIN_PRIORITY = 1;
    public static final int MAX_PRIORITY = 99;
    public static final int DEFAULT_PRIORITY = MIN_PRIORITY;

    private final Metadata metadata;

    public Plugin() {
        this.metadata = init();
    }

    public abstract Metadata init();

    public String getName() {
        return metadata.getName();
    }

    public String getDescription() {
        return metadata.getDescription();
    }

    public String getVersion() {
        return metadata.getVersion();
    }

    @Data
    public static class Metadata {
        public Metadata(String name, String description, String version) {
            this.name = name;
            this.description = description;
            this.version = version;
        }

        private String name;
        private String description;
        private String version;
        private int priority = DEFAULT_PRIORITY; // 插件加载优先级，默认为 1
    }
}
