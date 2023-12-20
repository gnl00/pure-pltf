# pure-pltf
> 一个插件化平台，支持动态加载和卸载 Jar 包插件。

## 目录介绍

```shell
pure-pltf
├── pure-pltf-base 插件接口定义
├── pure-pltf-core 核心功能实现
├── pure-pltf-lite 包含核心功能的最小实现
├── pure-pltf-plugin-example 插件实现样例
├── pure-pltf-web web-ui
```

...

适用场景：可以在 core 的基础上，根据甲方的要求，进行定制化开发。最终成品大概如下
```shell
pure-pltf
├── pure-pltf-base 插件接口定义
├── pure-pltf-AAA 基于 core，实现甲方 A 的需求
    ├── common-plugin-1
    ├── common-plugin-2
    ├── 甲方 A 定制插件 plugin-a-1
    ├── 甲方 A 定制插件 plugin-a-2
    ├── ...
├── pure-pltf-BBB 基于 core，实现甲方 B 的需求
    ├── common-plugin-1
    ├── common-plugin-2
    ├── 甲方 B 定制插件 plugin-b
    ├── ...
├── ...
```

...

---

## 如何运行

1、按顺序将 pure-pltf-base 和 pure-pltf-core 打包安装到 maven 仓库

2、动态 jar 包安装

将 pure-pltf-spi-impl 打包

2.1、手动安装

将 pure-pltf-spi-impl 的 target 目录下的 jar 包拷贝到根目录下。
访问 `http://localhost:8080/jars` 可以看到刚刚拷贝的 jar 包

```json
{
    "pure-pltf-spi-impl-1.0-SNAPSHOT": ".\pure-pltf-spi-impl-1.0-SNAPSHOT.jar"
}
```

访问 `http://localhost:8080/plugin/installFromRoot?pkgName=pure-pltf-plugin-example-1.0-SNAPSHOT.jar` 安装 jar 包

2.2、自动安装

将 pure-pltf-spi-impl 的 target 目录下 jar 包拷贝到根目录的 plugins 文件夹下。
应用启动完成后会通过 ApplicationReadyEvent 来自动加载 plugins 文件夹下的 jar 包。
可以在配置文件中自定义插件文件夹

```yaml
pltf:
  pluginDir: './plugins'
```


> 后续会通过插件元数据：版本号来判断是否需要更新插件；判断插件加载优先级等。

3、访问 `http://localhost:8080/plugin/execute?pluginName=` 执行插件

4、访问 `http://localhost:8080/plugin/uninstall?pluginName=` 卸载插件

...

---

## 功能展望

- [x] 信息收集与扩展接口
- [x] 插件手动安装
- [x] 插件自动加载
- [x] 插件卸载
- [ ] 插件功能实现
- [ ] 插件触发时机
- [ ] 完成 Web 界面
- [ ] 插件模板

...

---

## 插件接口规范

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

## 插件有什么用？

插件可以用来扩展应用的功能，比如：添加 Websocket 支持。不需要再另外启动一个服务，只需要在插件执行入口中添加好代码逻辑即可。

...

---

## 插件触发时机

* 手动触发（手动点击按钮）
* 自动触发（比如应用加载完成后即启动），自动触发需要给插件预留应用级别的 Hook 函数