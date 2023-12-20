package com.pure;

import java.util.ServiceLoader;

/**
 * InfoHandler<T> 泛型是添加的自定义 Info 类型
 *
 * @author gnl
 * @date 2023/5/7
 */
public abstract class InfoHandler<T extends BaseInfo> {

    private static BaseInfo sharedAdditionInfo;

    protected void setAdditionalInfo(T info){
        System.out.println("setting additional info");
        System.out.println(info);
        sharedAdditionInfo = info;
    }

    // 暴露给子类实现。插件可以实现此方法，返回自定义的信息
    public void addAdditionalInfo() {}

    public BaseInfo getAdditionalInfo() {
        // find all children
        findResources();
        return sharedAdditionInfo;
    }

    private void findResources() {
        ServiceLoader<? extends InfoHandler> services = ServiceLoader.load(InfoHandler.class);
        for (InfoHandler service : services) {
            service.addAdditionalInfo();
        }
    }
}
