package com.pure.handler;

import com.pure.Plugin;
import com.pure.classloader.DynamicClassLoader;
import com.pure.plugin.WrappedPlugin;
import com.pure.classloader.DynamicLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
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

    public static final int INSTALL_SUCCESS = 1;
    public static final int INSTALL_FAILED = -1;
    public static final int ALREADY_EXIST = 0;

    private final static Map<String, WrappedPlugin> PLUGINS = new HashMap<>();
    private static DynamicLoader dynamicLoader;

    static {
        dynamicLoader = new DynamicLoader();
    }

    @Autowired
    private ApplicationContext ac;

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
        DynamicClassLoader dcl = new DynamicClassLoader(url);
        Plugin plugin = dynamicLoader.loadOne(dcl.instance(), Plugin.class);

        if (Objects.nonNull(plugin)) {
            // check if plugin already installed
            String plgName = plugin.getName();

            if (PLUGINS.containsKey(plgName)) {
                log.info("plugin {} already installed", plgName);
                return ALREADY_EXIST;
            }
            // add plugin to IoC
            if (ac instanceof ConfigurableApplicationContext cac) {
                log.info("+++++ Autowire plugin to IOC +++++");
                plugin.setApplicationContext(ac);
                cac.getBeanFactory().registerSingleton(plgName, plugin);
            }

            dcl.setPlugin(plugin);
            // add a wrapper for plugin and classloader, to avoid classloader leak
            WrappedPlugin wp = new WrappedPlugin();
            wp.setPlugin(plugin);
            wp.setPluginLoader(dcl);
            wp.setIsEnable(true);

            PLUGINS.put(plgName, wp);
            log.info("plugin installed: {}", plgName);
            return INSTALL_SUCCESS;
        }
        return INSTALL_FAILED;
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

        log.info("+++++ Checking ClassLoader status +++++");
        log.info("status: {}", plg.getPluginLoader());
        log.info("===== plugin {} uninstalled =====", plgName);
        return 1;
    }

    public int execute(String plgName) {
        Plugin plg = null;
        WrappedPlugin wp = PLUGINS.get(plgName);
        if (Objects.isNull(wp)  || !wp.getIsEnable() || Objects.isNull(plg = wp.getPlugin())) {
            return -1;
        }
        plg.exec();
        return 1;
    }

}
