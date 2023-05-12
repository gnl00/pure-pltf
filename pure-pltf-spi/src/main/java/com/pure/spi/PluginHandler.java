package com.pure.spi;

import com.pure.base.Plugin;

/**
 * PluginInitializing
 *
 * @author gnl
 * @since 2023/5/12
 */
public interface PluginHandler extends Plugin {
    void onLoad(); // onload while container is ready

    void exec();
}
