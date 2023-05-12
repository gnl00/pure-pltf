package com.pure.impl;

import com.pure.spi.InfoHandler;

/**
 * InfoSpiImpl
 *
 * @author gnl
 * @since 2023/5/11
 */
public class InfoHandlerImpl implements InfoHandler {
    @Override
    public void load() {
        System.out.println("InfoSpiImpl loaded");
    }
}
