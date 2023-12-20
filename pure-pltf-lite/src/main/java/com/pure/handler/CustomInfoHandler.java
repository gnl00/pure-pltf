package com.pure.handler;

import com.pure.InfoHandler;
import com.pure.entity.CustomInfo;

/**
 * 添加额外的系统信息
 *
 * 注意：
 * 1、core 模块使用 SPI 加载此类，要实现添加自定义信息的功能，需要在 META-INF/services/com.pure.InfoHandler 添加对应信息
 * 2、CustomInfo 需要是 com.pure.BaseInfo 的子类
 */
public class CustomInfoHandler extends InfoHandler<CustomInfo> {
    @Override
    public void addAdditionalInfo() {
        CustomInfo info = new CustomInfo();
        info.setCusInfo1("qwe");
        info.setCusInfo2("123");
        info.setCusInfo3("!@#");

        setAdditionalInfo(info);
    }
}
