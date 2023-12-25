package com.example;

import com.pure.Plugin;
import org.springframework.context.annotation.ComponentScan;

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
}
