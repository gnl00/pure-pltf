# 功能设计

## 基础接口

### 系统信息接口

1、默认获取的信息

顶级信息收集接口 InfoHandler 暴露信息收集方法，默认实现类 DefaultInfoService 负责收集平台系统信息以及应用的信息。

> 部分系统信息可以从 /actuator/* 获取。

...

---

2、自定义系统信息

此外，如果想新增更多系统信息，可以实现 InfoHandler 接口
```java
/**
 * 添加额外的系统信息
 * 注意：
 * 1、core 模块使用 SPI 加载此类，要实现添加自定义信息的功能，需要在 META-INF/services/com.pure.handler.InfoHandler 添加对应信息
 * 2、CustomInfo 需要是 com.pure.base.BaseInfo 的子类
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
```