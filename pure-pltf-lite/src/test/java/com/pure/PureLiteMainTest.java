package com.pure;

import com.pure.config.ActuatorConfig;
import com.pure.entity.info.AppInfo;
import com.pure.handler.DefaultInfoHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.CompositeHealth;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

/**
 * PureLiteMainTest
 *
 * @author gnl
 * @date 2023/5/7
 */

@Slf4j
@SpringBootTest
public class PureLiteMainTest {

    @Autowired
    private DefaultInfoHandler infoService;

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Test
    public void test() {
        AppInfo appInfo = infoService.getAppInfo();
        System.out.println(appInfo);
    }

    @Test
    public void test2() {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        Map<String, Object> systemEnvironment =
                environment.getSystemEnvironment();
        System.out.println("systemEnvironment ===> ");
        System.out.println(systemEnvironment);

        Map<String, Object> systemProperties = environment.getSystemProperties();
        System.out.println("systemProperties ===> ");
        System.out.println(systemProperties);
    }

    // @Value("${${management.server.port:server.port}:8080}")
    // @Value("#{${management.server.port} ?: ${server.port} ?: 8080}")
    private String managementServerPort;

    @Test
    public void test3() {
        String actuatorPort = applicationContext.getEnvironment().getProperty("management.server.port");
        String serverPort = applicationContext.getEnvironment().getProperty("server.port");
        System.out.println(actuatorPort);
        System.out.println(serverPort);
    }

    @Autowired
    private ActuatorConfig actuatorInfo;

    @Test
    public void test4() {

        CompositeHealth health = (CompositeHealth) actuatorInfo.getHealthEndpoint().health();

        Map<String, HealthComponent> components = health.getComponents();
        Health diskSpace = (Health) components.get("diskSpace");
        System.out.println(diskSpace);
        Map<String, Object> details = diskSpace.getDetails();
        components.forEach((k, val) -> {
            log.info("key => {}, value => {}", k, val);
        });

        System.out.println(health.getStatus());

//        System.out.println(actuatorInfo.getHealthEndpoint().health().getStatus());
//        System.out.println(actuatorInfo.getHealthEndpoint().health());

        System.out.println(actuatorInfo.getInfoEndpoint().info());
    }

}
