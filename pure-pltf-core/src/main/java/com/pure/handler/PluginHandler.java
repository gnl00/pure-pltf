package com.pure.handler;

import com.pure.Plugin;
import com.pure.WrappedPlugin;
import com.pure.loader.DynamicLoader;
import com.pure.loader.TestClassLoader;
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
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class PluginHandler {

    private final static Map<String, WrappedPlugin> PLUGINS = new HashMap<>();
    private static DynamicLoader dynamicLoader;

static {
        dynamicLoader = new DynamicLoader();
    }

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

    public int install(URL url) {
        // load plugin
        TestClassLoader classLoader = new TestClassLoader(new URL[]{url});
        for (Plugin plugin : ServiceLoader.load(Plugin.class, classLoader)) {
            System.out.println(plugin);
        }

//        ServiceLoader<Plugin> plugins = dynamicLoader.load(dcl.instance(), Plugin.class);
//        Plugin plugin = plugins.stream().findFirst().get().get();
//        System.out.println(plugin);

        // check if plugin already installed
//        String plgName = plugin.getName();
//
//        if (PLUGINS.containsKey(plgName)) {
//            log.info("plugin {} already installed", plgName);
//            return -1;
//        }
//
//        dcl.setPlugin(plugin);
//        // add a wrapper for plugin and classloader, to avoid classloader leak
//        WrappedPlugin wp = new WrappedPlugin();
//        wp.setPlugin(plugin);
//        wp.setPluginLoader(dcl);
//        wp.setIsEnable(true);
//
//        PLUGINS.put(plgName, wp);
//        log.info("plugin installed {}", plgName);
        return 1;
    }

    private int installOne(URL url) {
        return -1;
    }

    public int installBatch(URL url) {
        int installFlag = 0;
        log.info("+++++ parsing plugins +++++");
        // ServiceLoader.load 的时候会执行 Plugin 无参构造方法
        for (Plugin plugin : ServiceLoader.load(Plugin.class)) {
            // do something
        }
        return installFlag;
    }

    public Map<String, Plugin> listInstalled() {
        return PLUGINS.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getPlugin()));
    }

    public int uninstall(String plgName) {
        WrappedPlugin plg = null;
        if (Objects.isNull(plg = PLUGINS.get(plgName))) {
            return -1;
        }
        PLUGINS.remove(plgName);
        plg.release();
        return 1;
    }

    public int execute(String plgName) {
        WrappedPlugin plg = PLUGINS.get(plgName);
        if (Objects.isNull(plg)) {
            return -1;
        }
        ClassLoader cl = plg.getPluginLoader();
        if (Objects.isNull(dynamicLoader)) {
            dynamicLoader = new DynamicLoader();
        }
        dynamicLoader.load(cl);
        return 1;
    }

}
