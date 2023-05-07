package com.pure;

import com.pure.comp.ActuatorInfo;
import com.pure.entity.info.BaseInfo;
import com.pure.service.impl.InfoServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

/**
 * PureLiteMainTest
 *
 * @author gnl
 * @since 2023/5/7
 */

@SpringBootTest
public class PureLiteMainTest {

    @Autowired
    private InfoServiceImpl infoService;

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Test
    public void test() {
        BaseInfo baseInfo = infoService.getBaseInfo();
        System.out.println(baseInfo);
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
    private ActuatorInfo actuatorInfo;

    @Test
    public void test4() {
        System.out.println(actuatorInfo);

        HealthComponent health = actuatorInfo.getHealthEndpoint().health();

        System.out.println(actuatorInfo.getHealthEndpoint().health().getStatus());
        System.out.println(actuatorInfo.getHealthEndpoint().health());

        System.out.println(actuatorInfo.getInfoEndpoint().info());
    }

}
