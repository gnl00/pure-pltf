package com.pure.impl;

import com.pure.spi.SQLHandler;

/**
 * SQLHandlerImpl
 *
 * @author gnl
 * @since 2023/5/11
 */
public class SQLHandlerImpl implements SQLHandler {
    @Override
    public void load() {
        System.out.println("InfoSpiImpl loaded");
    }
}
