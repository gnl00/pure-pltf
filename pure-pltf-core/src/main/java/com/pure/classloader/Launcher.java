package com.pure.classloader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Launcher extends Thread {

    private final String mainClassName;

    private final String[] args;


    Launcher(ClassLoader classLoader, String mainClassName, String[] args) {
        this.mainClassName = mainClassName;
        this.args = args;
        setName("Launcher");
        setDaemon(false);
        setContextClassLoader(classLoader);
    }

    @Override
    public void run() {
        try {
            ClassLoader cl = getContextClassLoader();
            System.out.println("classloader in launcher#run cl: " + cl);
            Class<?> aClass = cl.loadClass(this.mainClassName);
            Class<?> mainClass = Class.forName(this.mainClassName, false, cl);
            Method mainMethod = mainClass.getDeclaredMethod("main", String[].class);
            mainMethod.setAccessible(true);
            mainMethod.invoke(null, new Object[]{this.args});
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            System.out.println("Launcher error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
