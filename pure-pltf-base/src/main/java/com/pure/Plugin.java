package com.pure;

import lombok.Data;
import lombok.ToString;

@ToString
public abstract class Plugin implements IPlugin {

    private final Metadata metadata;

    public Plugin() {
        System.out.println("Plugin no args constructor");
        this.metadata = init();
    }

    public abstract Metadata init();

    public String getName() {
        return metadata.name;
    }

    public String getDesc() {
        return metadata.description;
    }

    public String getVersion() {
        return metadata.version;
    }

    @Data
    public static class Metadata {

        public Metadata() {}

        public Metadata(String name, String description, String version) {
            this.name = name;
            this.description = description;
            this.version = version;
        }

        private String name;
        private String description;
        private String version;
    }
}
