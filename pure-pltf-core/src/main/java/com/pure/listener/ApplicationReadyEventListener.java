package com.pure.listener;

import com.pure.loader.TestClassLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

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

    @Value("${pltf.plugin-dir}")
    private String pluginDir;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("***** Auto load plugins *****");
        try {
            loadPlugins();
        } catch (MalformedURLException e) {
            log.error("auto load plugins error: {}", e.getMessage());
        }
    }

    private void loadPlugins() throws MalformedURLException {
        File pluginsDir = new File(pluginDir);
        for (File pluginFile : pluginsDir.listFiles()) {
            URL url = pluginFile.toURI().toURL();
            // TODO 实现 jar 包自动加载
        }

//        TestClassLoader cl = prepareClassLoader();
//        File pluginDirectory = new File(CoreConstant.EXTERNAL_JAR_DIR);
//        String[] files = pluginDirectory.list();
//
//        String filePath = CoreConstant.EXTERNAL_JAR_DIR;
//        for (String filename : files) {
//            String fullPath = filePath + filename;
//            File plugin = new File(fullPath);
//
//            // TODO cl.loadExternalJar(plugin);
//
//            log.info("Loaded plugin ==> {}", fullPath);
//        }
    }
}
