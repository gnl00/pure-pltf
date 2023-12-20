package com.pure.beans;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointProperties;
import org.springframework.stereotype.Component;

@Component
public class HealthEndpointPropertiesPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof HealthEndpointProperties) {
            HealthEndpointProperties healthEndpoint = (HealthEndpointProperties) bean;
            healthEndpoint.setShowDetails(HealthEndpointProperties.Show.ALWAYS);
        }
        return bean;
    }
}
