package com.pure.impl;

import com.pure.spi.SQLPlugin;

/**
 * SQLHandlerImpl
 *
 * @author gnl
 * @date 2023/5/11
 */
public class SQLHandlerImpl implements SQLPlugin {
    @Override
    public void load() {
        System.out.println("InfoSpiImpl loaded");
    }

    @Override
    public void exec() {

    }
}
