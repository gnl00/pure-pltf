package com.pure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.EnvironmentInfoContributor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.client.RestTemplate;

/**
 * CoreConfig
 *
 * @author gnl
 * @since 2023/5/7
 */
@Configuration
public class CoreConfig {

    @Autowired
    private ConfigurableApplicationContext ac;

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

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getValueFromProperties(String key) {
        return ac.getEnvironment().getProperty(key);
    }

}
