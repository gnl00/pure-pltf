# pure-pltf
> 一个插件化平台，支持动态加载和卸载 Jar 包插件。

## 目录介绍

```shell
pure-pltf
├── pure-pltf-base 基础定义
├── pure-pltf-core 核心功能实现
├── pure-pltf-lite 包含核心功能的最小实现
├── pure-pltf-spi  SPI 接口模块。暴露 BootSpi#load 方法给子类实现。类似 jdbc。
├── pure-pltf-spi-impl  SPI 实现模块 1，依赖 SPI 接口模块。类似 mysql-connector-java，是 jdbc 的 mysql 实现。
├── pure-pltf-spi-implXXX  SPI 接口模块 2，依赖 SPI 接口模块。类似 postgresql-connector-java，是 jdbc 的 pg 实现。
├── pure-pltf-web 通过 Web 页面实现动态加载和卸载 Jar 包插件
```

适用场景：可以在 core 的基础上，根据甲方的要求，进行定制化开发。最终成品大概如下
```shell
pure-pltf
├── pure-pltf-base 基础组件
├── pure-pltf-core 核心功能
├── pure-pltf-AAA 基于 core，实现甲方 A 的需求
├── pure-pltf-BBB 基于 core，实现甲方 B 的需求
├── ...
```

...

---

## 如何运行

1、按顺序将 pure-pltf-base 和 pure-pltf-core 打包安装到 maven 仓库

2、运行 pure-pltf-lite，访问 `http://localhost:8080/lite` 查看 lite 的运行状态

3、动态 jar 包安装

将 pure-pltf-spi-impl 打包

3.1、手动安装

将 pure-pltf-spi-impl 的 target 目录下的 jar 包拷贝到根目录下。
访问 `http://localhost:8080/jars` 可以看到刚刚拷贝的 jar 包

```json
{
    "pure-pltf-spi-impl-1.0-SNAPSHOT": ".\pure-pltf-spi-impl-1.0-SNAPSHOT.jar"
}
```

访问 `http://localhost:8080/install?jarName=` 安装 jar 包

3.2、自动安装

将 pure-pltf-spi-impl 的 target 目录下 jar 包拷贝到根目录的 plugins 文件夹下。
应用启动完成后会通过 ApplicationReadyEvent 来自动加载 plugins 文件夹下的 jar 包。

> 后续可以给插件添加一些元数据：通过版本号来判断是否需要更新插件；判断插件加载优先级等。

4、访问 `http://localhost:8080/execute?pluginName=` 执行插件

5、访问 `http://localhost:8080/uninstall?pluginName=` 卸载插件

...

---

## 功能展望

- [x] 信息收集与扩展接口
- [ ] 插件手动安装
- [ ] 插件自动安装
- [ ] 插件卸载
- [ ] 插件触发时机
- [ ] 完成 Web 界面
- [ ] 插件模板

...

---

## 插件接口规范

Plugin 接口是所有插件的顶级接口。

...

---

## 插件触发时机

* 手动触发（手动点击按钮）
* 自动触发（比如应用加载完成后即启动），自动触发需要给插件预留应用级别的 Hook 函数