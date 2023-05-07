package com.demo.spi.impl;

import com.demo.spi.CusSpi;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/5/7
 */
public class CusSpiImplB implements CusSpi {
    @Override
    public void load() {
        System.out.println("BBB load");
    }
}
