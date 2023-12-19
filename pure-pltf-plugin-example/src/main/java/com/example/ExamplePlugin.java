package com.example;

import com.pure.Plugin;

public class ExamplePlugin extends Plugin {
    @Override
    public void exec() {
        System.out.println("executing example plugin...");
    }


    @Override
    public Plugin.Metadata init() {
        return new Metadata("example", "example plugin", "1.0.0");
    }
}
