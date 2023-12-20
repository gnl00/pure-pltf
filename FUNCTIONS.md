# 功能设计

## 系统信息接口

1、默认获取的信息

顶级信息收集接口 InfoHandler 暴露信息收集方法，默认实现类 DefaultInfoHandler 负责收集平台系统信息以及应用的信息。

> 部分系统信息可以从 /actuator/* 获取。

...

---

2、自定义系统信息

此外，如果想新增更多系统信息，可以实现 InfoHandler 接口
```java
/**
 * 添加额外的系统信息
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
```

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomInfo implements BaseInfo, Serializable {
    private static final long serialVersionUID = 7603067916535124527L;
    private String cusInfo1;
    private String cusInfo2;
    private String cusInfo3;
}
```

...

> 信息拓展接口是为平台设计的。可以在仓库代码中看到，只有 [pure-pltf-lite](pure-pltf-lite) 需要添加拓展信息的时候实现了 InfoHandler。
> 后续是否会添加插件对信息扩展的支持？
> 插件设计的时候并没有考虑到【添加拓展信息】这个职能，暂未有添加支持的打算。

...

---

## 插件接口

Plugin 接口是所有插件的顶级接口；PluginHook 提供插件运行期间的一些 Hook 方法。

> 【**强制要求**】：一个插件只能有一个 Plugin 实现类。

一个基础的插件实现如下：

1、定义
```java
package com.example;

import com.pure.Plugin;

public class ExamplePlugin extends Plugin {
    @Override
    public void exec() {
        System.out.println("executing example plugin...");
    }


    @Override
    public Plugin.Metadata init() {
        return new Metadata("example", "example plugin", "1.0.0");
    }
}
```

2、配置 `META-INF/services/com.pure.Plugin`，内容如下
```
com.example.ExamplePlugin
```

3、打包成 jar 包，接下来可选择放到根目录下的 plugins 文件夹下自动加载或者通过 url 手动加载插件。

...

---