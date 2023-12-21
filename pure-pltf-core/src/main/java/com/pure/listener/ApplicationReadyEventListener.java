package com.pure.listener;

import com.pure.Plugin;
import com.pure.beans.PltfProperties;
import com.pure.handler.PluginHandler;
import com.pure.classloader.DynamicClassLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

/**
 * 在应用启动时扫描 plugins 目录下的 jar 包
 * <p> 注意：目前仅仅实现了扫描，并未实现加载
 *
 * @author gnl
 * @date 2023/5/11
 */
@Slf4j
@Component
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    private int pluginCount = 0;

    @Autowired
    private PltfProperties pltfProperties;

    @Autowired
    PluginHandler pluginHandler;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("+++++ Auto load plugins +++++");
        log.info("plugin dir: {}", pltfProperties.getPluginDir());
        try {
            loadPlugins();
            log.info("===== Auto load {} plugin(s) =====", pluginCount);
        } catch (MalformedURLException e) {
            log.error("auto load plugins error: {}", e.getMessage());
        }
    }

    private void loadPlugins() throws MalformedURLException {
        File pluginsDir = new File(pltfProperties.getPluginDir());
        if (!pluginsDir.exists()) {
            return;
        }

        if (!pluginsDir.isDirectory()) {
            return;
        }

        for (File pluginFile : Objects.requireNonNull(pluginsDir.listFiles())) {
            Plugin plugin = null;
            DynamicClassLoader dcl = null;

            int check = -1;
            if (Objects.nonNull(pluginFile) && pluginFile.exists() && pluginFile.isFile()) {
                URL url = pluginFile.toURI().toURL();
                check = pluginHandler.install(url);
            }

            if (check == 1) {
                pluginCount++;
            }
        }
    }

    public int getPluginCount() {
        return pluginCount;
    }
}
