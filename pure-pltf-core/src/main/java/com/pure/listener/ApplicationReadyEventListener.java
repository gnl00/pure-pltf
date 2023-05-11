package com.pure.listener;

import com.pure.classloader.SPIClassLoader;
import com.pure.global.GlobalConstant;
import com.pure.global.GlobalRef;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

/**
 * ApplicationReadyEventListener
 *
 * @author gnl
 * @since 2023/5/11
 */
@Slf4j
@Component
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("onApplicationEvent [received]");
        log.info("loading plugins");
        try {
            loadPlugins();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private SPIClassLoader prepareClassLoader() {
        if (Objects.isNull(GlobalRef.pluginClassLoader)) {
            GlobalRef.pluginClassLoader = new SPIClassLoader(new URL[]{}, getClass().getClassLoader());
        }
        return GlobalRef.pluginClassLoader;
    }

    private void loadPlugins() throws MalformedURLException {
        SPIClassLoader cl = prepareClassLoader();
        File pluginDirectory = new File(GlobalConstant.EXTERNAL_JAR_DIR);
        String[] files = pluginDirectory.list();

        String filePath = GlobalConstant.EXTERNAL_JAR_DIR;
        for (String filename : files) {
            String fullPath = filePath + filename;
            File plugin = new File(fullPath);
            cl.loadExternalJar(plugin);
            log.info("Loaded plugin: {}", fullPath);
        }
    }

}
