package com.pure.base;

public interface PluginBase {

    default void beforeLoad() {}

    default void load() {}

    default void onLoad() {}

    default void afterLoad() {}

    default void beforeExec() {}

    default void exec() {}

    default void afterExec() {}

    default void beforeUnload() {}
    default void unload() {}
    default void afterUnload() {}
}
