package com.pure.classloader;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.lang.reflect.Method;
import java.net.URL;

public class Restarter {

    private static final Object INSTANCE_MONITOR = new Object();

    private static Restarter instance;

    private final String[] args;
    private final String mainClassName;


    private boolean finish = false;

    private Restarter(String[] args) {
        this.args = args;
        this.mainClassName = getMainClassName(Thread.currentThread());
    }

    public static void initialize(String[] args) {
        Restarter localInstance = null;
        synchronized (INSTANCE_MONITOR) {
            if (instance == null) {
                localInstance = new Restarter(args);
                instance = localInstance; // 巧妙避免 ApplicationStartingEvent 循环加载
            }
        }
        if (localInstance != null) {
            localInstance.immediateRestart();
        }
    }

    public static Restarter getInstance() {
        synchronized (INSTANCE_MONITOR) {
            return instance;
        }
    }

    private void immediateRestart() {
        System.out.println("Immediately restarting application");
        try {
            start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void start() throws InterruptedException {
        PltfClassLoader pltfClassLoader = new PltfClassLoader(new URL[]{}, getClass().getClassLoader());
        Launcher launcher = new Launcher(pltfClassLoader, mainClassName, args);
        launcher.start();
        launcher.join(); // Waits for this thread to die
    }

    private String getMainClassName(Thread currentThread) {
        Method mainMethod = getMainMethod(currentThread);
        if (null == mainMethod) {
            System.out.println("Unable to find main method");
            return null;
        }
        return mainMethod.getDeclaringClass().getName();
    }

    private Method getMainMethod(Thread currentThread) {
        StackTraceElement[] stackTrace = currentThread.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if ("main".equals(element.getMethodName())) {
                Method method = getMainMethod(element);
                return method;
            }
        }
        return null;
    }

    private Method getMainMethod(StackTraceElement element) {
        try {
            Class<?> mainClass = Class.forName(element.getClassName());
            return mainClass.getMethod("main", String[].class);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            System.out.println("get main method error " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean isFinish() {
        return this.finish;
    }

    public void finish() {
        synchronized (INSTANCE_MONITOR) {
            if (!isFinish()) {
                this.finish = true;
            }
        }
    }

    public void prepare(ConfigurableApplicationContext applicationContext) {
        if (null != applicationContext && null != applicationContext.getParent()) {
            return;
        }
        if (applicationContext instanceof GenericApplicationContext genericApplicationContext) {
            prepare(genericApplicationContext);
        }
    }

    private void prepare(GenericApplicationContext applicationContext) {
        // applicationContext.setResourceLoader();
    }
}
