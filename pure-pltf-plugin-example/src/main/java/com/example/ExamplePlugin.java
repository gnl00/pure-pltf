package com.example;

import com.pure.Plugin;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;
import java.util.Map;

@ComponentScan(basePackages = "com.example")
public class ExamplePlugin extends Plugin {

    @Override
    public void exec() {
        System.out.println(getApplicationContext());
        System.out.println("executing example plugin...");
    }

    @Override
    public Plugin.Metadata init() {
        return new Metadata("example", "example plugin", "1.0.0");
    }

    public Map<String, Object> registerBean() {
        // Map<String, RegisterBean> registerBeans = new HashMap<>();
        // MyBean implement RegisterBean
        // registerBeans.put(beanName, MyBean);
        // ...
        // TODO: register bean from plugin into ioc container
        return null;
    }
}
