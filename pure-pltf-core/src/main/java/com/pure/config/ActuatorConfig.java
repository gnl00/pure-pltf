package com.pure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.context.annotation.Configuration;


/**
 * ActuatorConfig
 *
 * @author gnl
 * @since 2023/5/7
 */
@Slf4j
@Configuration
public class ActuatorConfig {

    private final HealthEndpoint healthEndpoint;
    private final InfoEndpoint infoEndpoint;

    public ActuatorConfig(HealthEndpoint healthEndpoint, InfoEndpoint infoEndpoint) {
        this.healthEndpoint = healthEndpoint;
        this.infoEndpoint = infoEndpoint;
    }

    public HealthEndpoint getHealthEndpoint() {
        return healthEndpoint;
    }

    public InfoEndpoint getInfoEndpoint() {
        return infoEndpoint;
    }
}
