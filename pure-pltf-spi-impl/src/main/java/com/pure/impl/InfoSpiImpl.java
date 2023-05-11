package com.pure.impl;

import com.pure.spi.InfoSpi;

/**
 * InfoSpiImpl
 *
 * @author gnl
 * @since 2023/5/11
 */
public class InfoSpiImpl implements InfoSpi {
    @Override
    public void load() {
        System.out.println("InfoSpiImpl loaded");
    }
}
