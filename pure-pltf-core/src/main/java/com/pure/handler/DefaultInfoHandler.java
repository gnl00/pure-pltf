package com.pure.handler;

import com.pure.BaseInfo;
import com.pure.InfoHandler;
import com.pure.config.ActuatorConfig;
import com.pure.entity.info.AppInfo;
import com.pure.entity.info.SysInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.CompositeHealth;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.pure.constant.CoreConstant.*;

/**
 * DefaultInfoHandler
 *
 * @author gnl
 * @date 2023/5/7
 */
@Slf4j
@Component
public class DefaultInfoHandler extends InfoHandler {

    @Autowired
    private ConfigurableApplicationContext ac;

    @Value("${info.app.name}")
    private String appName;

    @Value("${info.app.version}")
    private String appVersion;

    @Autowired
    private ActuatorConfig actuatorInfo;

    public AppInfo getAppInfo() {

        AppInfo appInfo = AppInfo.builder()
                .appName(appName)
                .appVersion(appVersion)
                .build();

        // TODO getBaseInfoFromPlugin();

        return appInfo;
    }

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

    @Override
    public BaseInfo getAdditionalInfo() {
        return super.getAdditionalInfo();
    }

    private void getInfoFromActuator(SysInfo sysInfo) {
        CompositeHealth health = (CompositeHealth) actuatorInfo.getHealthEndpoint().health();
        Map<String, Object> info = actuatorInfo.getInfoEndpoint().info();
        String serverStatus = health.getStatus().getCode();
        sysInfo.setStatus(serverStatus);
    }

//    private void getInfoFromSpi() {
//        ServiceLoader<InfoHandler> infoHandlers = ServiceLoader.load(InfoHandler.class);
//
//        // no spi implement
//        if (infoHandlers.findFirst().orElse(null) == null) {
//            return;
//        }
//
//        // found spi implement
//        for (InfoHandler infoHandler : infoHandlers) {
//            // gathering ext info
//            // ...
//        }
//    }
}
