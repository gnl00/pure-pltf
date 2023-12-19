package com.pure;

/**
 * PluginHook
 *
 * @author gnl
 * @date 2023/12/15
 */
public interface PluginHook {

    default void beforeLoad() {}

    default void load() {}

    default void onLoad() {}

    default void afterLoad() {}

    default void beforeExec() {}

    void exec();

    default void afterExec() {}

    default void beforeUnload() {}
    default void unload() {}
    default void afterUnload() {}
}
