package com.pure.service.impl;

import com.pure.comp.ActuatorInfo;
import com.pure.entity.info.BaseInfo;
import com.pure.entity.info.SysInfo;
import com.pure.service.InfoService;
import com.pure.spi.InfoHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.CompositeHealth;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.Status;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
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

    // env properties key
    private static final String JAVA_RUNTIME_VERSION = "java.runtime.version";
    private static final String OS_NAME = "os.name";
    private static final String OS_ARCH = "os.arch";
    private static final String OS_VERSION = "os.version";
    private static final String USER_COUNTRY = "user.country";
    private static final String USER_LANGUAGE = "user.language";
    private static final String USER_TIMEZONE = "user.timezone";

    // actuator key
    private static final String DISK_SPACE = "diskSpace";
    private static final String DISK_SPACE_TOTAL = "total";
    private static final String DISK_SPACE_FREE = "free";

    @Autowired
    private ConfigurableApplicationContext ac;

    @Value("${info.app.name}")
    private String appName;

    @Value("${info.app.version}")
    private String appVersion;

    @Value("${info.app.encoding}")
    private String appEncoding;

    @Autowired
    private ActuatorInfo actuatorInfo;

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
        String javaRuntimeVersion = (String) systemProperties.get(JAVA_RUNTIME_VERSION);
        String osName = (String) systemProperties.get(OS_NAME);
        String osArch = (String) systemProperties.get(OS_ARCH);
        String osVersion = (String) systemProperties.get(OS_VERSION);

        String country = (String) systemProperties.get(USER_COUNTRY);
        String language = (String) systemProperties.get(USER_LANGUAGE);
        String timezone = (String) systemProperties.get(USER_TIMEZONE);

        SysInfo sysInfo = SysInfo.builder()
                .javaRuntimeVersion(javaRuntimeVersion)
                .os(osName)
                .osArch(osArch)
                .osVersion(osVersion)
                .country(country)
                .language(language)
                .timezone(timezone)
                .build();

        getInfoFromActuator(sysInfo);

        return sysInfo;
    }

    private void getInfoFromActuator(SysInfo sysInfo) {
        CompositeHealth health = (CompositeHealth) actuatorInfo.getHealthEndpoint().health();
        String serverStatus = health.getStatus().getCode();

        sysInfo.setStatus(serverStatus);

        Map<String, HealthComponent> components = health.getComponents();
        Health diskSpaceHealth = (Health) components.get(DISK_SPACE);
        if (Objects.nonNull(diskSpaceHealth)) {
            Map<String, Object> diskDetails = diskSpaceHealth.getDetails();
            String diskTotal = String.valueOf(diskDetails.get(DISK_SPACE_TOTAL));
            String diskFree = String.valueOf(diskDetails.get(DISK_SPACE_FREE));

            sysInfo.setDiskStatus(diskSpaceHealth.getStatus().getCode());
            sysInfo.setDiskSpaceTotal(diskTotal);
            sysInfo.setDiskSpaceFree(diskFree);
        }
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
