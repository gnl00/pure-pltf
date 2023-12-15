package com.pure.handler;

import com.pure.base.PluginMetadata;
import com.pure.loader.DynamicClassLoader;
import com.pure.loader.DynamicLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Component
public class PluginHandler {

    private final static Map<String, PluginMetadata> PLUGINS = new HashMap<>();
    private static DynamicLoader dynamicLoader;

    public Map<String, Object> listRootJars() {
        Map<String, Object> jars = new HashMap<>();
        // get jars file from project root path
        try (Stream<Path> pathStream = Files.list(Paths.get("."))) {
            pathStream.filter(p -> p.toString().endsWith(".jar")).forEach(p -> {
                jars.put(p.getFileName().toString().substring(0, p.getFileName().toString().length() - 4), p.toString());
            });
        } catch (IOException e) {
            log.error("get jars from root error {}", e.getMessage());
        }
        return jars;
    }

    public int install(String plgName, URL url) {
        if (PLUGINS.containsKey(plgName)) {
            return -1;
        }
        DynamicClassLoader dcl = new DynamicClassLoader();
        PluginMetadata pm = PluginMetadata.builder()
                .name(plgName)
                .classLoader(dcl)
                .build();

        PLUGINS.put(plgName, pm);
        return 1;
    }

    public Map<String, PluginMetadata> listInstalled() {
        return PLUGINS;
    }

    public int uninstall(String plgName) {
        PluginMetadata pm = null;
        if (Objects.isNull(pm = PLUGINS.get(plgName))) {
            return -1;
        }
        PLUGINS.remove(plgName);
        pm.getClassLoader().release();
        return 1;
    }

    public int execute(String plgName) {
        PluginMetadata pm = PLUGINS.get(plgName);
        if (Objects.isNull(pm)) {
            return -1;
        }
        ClassLoader cl = pm.getClassLoaderInstance();
        if (Objects.isNull(dynamicLoader)) {
            dynamicLoader = new DynamicLoader();
        }
        dynamicLoader.load(cl);
        return 1;
    }

    private PluginMetadata parsePlugin() {
        // TODO 解析插件信息，设置到 PluginMetadata
        return null;
    }

}
