package com.example;

import org.springframework.context.annotation.Configuration;

// 因为是手动注册 Bean，@ComponentScan 不会生效，
// 所以 ExampleConfig 不会被自动注册到 IOC 容器中
@Configuration
public class ExampleConfig {

    {
        System.out.println("ExampleConfig init");
    }

}
