package com.pure;

import com.pure.spi.InfoSpi;

import java.util.ServiceLoader;

// @SpringBootApplication
public class PureCoreMain {
    public static void main(String[] args) {
        // SpringApplication.run(PureCoreMain.class, args);

        ServiceLoader<InfoSpi> services = ServiceLoader.load(InfoSpi.class);
        for (InfoSpi service : services) {
            service.load();
        }
    }
}