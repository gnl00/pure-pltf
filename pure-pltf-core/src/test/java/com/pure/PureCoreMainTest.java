package com.pure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

/**
 * PureCoreMainTest
 *
 * @author gnl
 * @since 2023/5/7
 */
@SpringBootTest
public class PureCoreMainTest {

    @Value("${info.java.version}")
    private String javaVersion;

    @Value("${info.app.encoding}")
    private String appEncoding;

    @Autowired
    ConfigurableApplicationContext applicationContext;

    @Test
    public void test() {
        System.out.println(javaVersion);
        System.out.println(appEncoding);
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

}
