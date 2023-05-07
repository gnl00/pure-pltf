package com.demo;

import com.demo.spi.CusSpi;

import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) {
        ServiceLoader<CusSpi> services = ServiceLoader.load(CusSpi.class);
        for (CusSpi service : services) {
            service.load();
        }
    }
}