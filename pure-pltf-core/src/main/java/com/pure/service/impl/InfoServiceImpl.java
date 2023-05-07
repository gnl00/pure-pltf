package com.pure.service.impl;

import com.pure.entity.info.BaseInfo;
import com.pure.entity.info.SysInfo;
import com.pure.service.InfoService;
import com.pure.spi.InfoHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.ServiceLoader;

/**
 * InfoServiceImpl
 *
 * @author gnl
 * @since 2023/5/7
 */
@Service
@Slf4j
public class InfoServiceImpl implements InfoService {

    @Autowired
    private ConfigurableApplicationContext ac;

    @Value("${info.app.name}")
    private String appName;

    @Value("${info.app.version}")
    private String appVersion;

    @Value("${info.java.version}")
    private String javaVersion;

    @Value("${info.app.encoding}")
    private String appEncoding;

    @Override
    public BaseInfo getBaseInfo() {

        BaseInfo baseInfo = BaseInfo.builder()
                .appName(appName)
                .appVersion(appVersion)
                .appEncoding(appEncoding)
                .build();

        getInfoFromSpi();

        return baseInfo;
    }

    @Override
    public SysInfo getSysInfo() {
        Map<String, Object> systemProperties = ac.getEnvironment().getSystemProperties();
        String javaRuntimeVersion = (String) systemProperties.get("java.runtime.version");
        String osName = (String) systemProperties.get("os.name");
        String osArch = (String) systemProperties.get("os.arch");
        String osVersion = (String) systemProperties.get("os.version");

        String country = (String) systemProperties.get("user.country");
        String language = (String) systemProperties.get("user.language");
        String timezone = (String) systemProperties.get("user.timezone");

        SysInfo sysInfo = SysInfo.builder()
                .javaRuntimeVersion(javaRuntimeVersion)
                .os(osName)
                .osArch(osArch)
                .osVersion(osVersion)
                .country(country)
                .language(language)
                .timezone(timezone)
                .build();

        return sysInfo;
    }

    private void getInfoFromSpi() {
        ServiceLoader<InfoHandler> infoHandlers = ServiceLoader.load(InfoHandler.class);

        // no spi implement
        if (infoHandlers.findFirst().orElse(null) == null) {
            return;
        }

        // found spi implement
        for (InfoHandler infoHandler : infoHandlers) {
            // gathering ext info
            // ...
        }
    }
}
