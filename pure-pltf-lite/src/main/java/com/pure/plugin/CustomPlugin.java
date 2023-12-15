package com.pure.plugin;

import com.pure.base.Plugin;

public class CustomPlugin extends Plugin {
    @Override
    public void exec() {
        System.out.println("executing custom plugin...");
    }
}
