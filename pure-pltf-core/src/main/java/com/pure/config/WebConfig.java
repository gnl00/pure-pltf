package com.pure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig
 *
 * @author gnl
 * @date 2023/5/11
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOrigins("http://localhost:5173");
    }
}
