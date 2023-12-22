package com.pure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PureLiteMain
 *
 * @author gnl
 * @since 2023/5/7
 */
@SpringBootApplication
public class PureLiteMain {

    private static final String APP_NAME = "pure-pltf-lite"; // 需要和当前目录名称一致

    private static int startCount = 0;

    public static void main(String[] args) {
        System.out.println("ClassLoader for now: " + PureLiteMain.class.getClassLoader());
        String[] arguments = new String[args.length + 1];
        for (int i = 0; i < args.length; i++) {
            arguments[i] = args[i];
        }
        arguments[arguments.length - 1] = APP_NAME;

        if (startCount < 2) {
            SpringApplication.run(PureLiteMain.class, arguments);
            startCount++;
        }
    }
}
