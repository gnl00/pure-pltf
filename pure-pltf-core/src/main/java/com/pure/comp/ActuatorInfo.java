package com.pure.comp;

import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.stereotype.Component;

/**
 * ActuatorInfo
 *
 * @author gnl
 * @since 2023/5/7
 */
@Component
public class ActuatorInfo {

    private final HealthEndpoint healthEndpoint;
    private final InfoEndpoint infoEndpoint;

    public ActuatorInfo(HealthEndpoint healthEndpoint, InfoEndpoint infoEndpoint) {
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
