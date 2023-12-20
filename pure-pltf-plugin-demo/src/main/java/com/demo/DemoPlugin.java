package com.demo;

import com.pure.Plugin;

public class DemoPlugin extends Plugin {
    @Override
    public Metadata init() {
        return new Metadata("demo", "a demo plugin", "1.0.0");
    }

    @Override
    public void exec() {
        System.out.println("hello world, this is a demo plugin");
    }
}
