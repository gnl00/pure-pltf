package com.pure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.info.EnvironmentInfoContributor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * CoreConfig
 *
 * @author gnl
 * @since 2023/5/7
 */
@Slf4j
@Configuration
public class CoreConfig {

    @Autowired
    private ConfigurableApplicationContext ac;

    @Autowired
    private WebEndpointProperties webEndpoint;

    @PostConstruct
    public void afterConstruct() {
        log.info("WebEndpointProperties expose: {}", "*");
        webEndpoint.getExposure().setInclude(Set.of("*"));
    }

    /**
     * 自定义 actuator/info 信息
     * EnvironmentInfoContributor 中有一句 binder.bind("info", STRING_OBJECT_MAP).ifBound(builder::withDetails);
     * 意思是绑定 application 配置文件中 info 开头的配置信息
     * 可以通过 http://host:port/actuator/info 访问到
     */
    @Bean
    public EnvironmentInfoContributor environmentInfoContributor() {
        ConfigurableEnvironment environment = ac.getEnvironment();
        return new EnvironmentInfoContributor(environment);
    }

    public String getValueFromProperties(String key) {
        return ac.getEnvironment().getProperty(key);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
