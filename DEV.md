# 开发历程

## 功能设计
### 信息收集
顶部接口 InfoHandler 暴露信息收集方法，core 中的默认实现类 DefaultInfoHandler 负责收集应用内的信息。
部分系统信息可以从 /actuator/* 获取。

<br>

**信息分类**
* 系统信息：运行环境，Java 版本，内存使用等信息
* 基本信息：项目信息
* 功能信息：支持的功能，功能启用状态

<br>

有两种方法可以获取 actuator 信息：
* RestTemplate
* 注入 HealthEndpoint 和 InfoEndpoint

<br>

使用 RestTemplate 需要用到项目端口号，actuator 的端口号 management.server.port 可能和 server.port 不同。
所以需要判断：如果存在 management.server.port 则使用，否则使用 server.port 否则使用默认端口号 8080。

要完成判断有两种方式：
* spEL
* 在代码获取配置值然后判断。

<br>

使用 spEL
```java
// spEL
@Value("#{${management.server.port} ?: ${server.port} ?: 8080}")
private String actuatorPort;
```
使用 spEL 的缺点是在配置文件中必须存在 `management.server.port` 和 `server.port` 这两个配置。**可以不设置值，但是必须存在**。否则启动项目的时候就会报错找不到这两个 key。
有一个解决办法是配置文件加上
```properties
spring.config.failOnMissingProperties=false
```
这样就可以关闭 Spring Boot 在启动时对配置项的检查，即使在代码中使用了未定义的配置项也不会报错了。
需要注意的是，关闭这个参数可能会导致配置出现错误而不被发现，可能会给应用程序带来不必要的风险。建议在开发环境中使用，生产环境尽量不要关闭。

...

<br>
在代码获取配置

```java
// 如果配置文件中不存在该 key 返回 null，不会报错
String actuatorPort = ac.getEnvironment().getProperty("management.server.port");
```

## 碎碎念
**Update-1**
> <br>
> 后端：
> 目前看来难度不大。主要用到了 Java 的 SPI 机制，只要暴露好接口，提供给外部实现即可。
> core 可能需要一个信息收集接口，由子项目实现。子项目可以直接从 pom.xml 获取基础信息。
>
> 前端：
> 可以从前端页面选择加载并安装对应的功能

**Update-2**

> 第一步，先做一个全平台的信息收集接口，可以全自动收集当前已加载的应用信息，以及收集安装的应用信息。
>
> *注意：开发过程中不要过度设计*

**Update-3**
> 之前的项目中用到 spring-boot-actuator 的时候，在配置文件中添加 info 就可以在 `host:port/actuator/info` 中读取到自定义的 info 信息。
> 当前项目中用到的 springboot 版本是 2.7.11，直接添加未生效。需要手动注入一个 EnvironmentInfoContributor
```java
@Configuration
public class CoreConfig {
    @Autowired
    private ConfigurableApplicationContext ac;
    /**
     * 自定义 actuator/info 信息
     * EnvironmentInfoContributor 中有一句 binder.bind("info", STRING_OBJECT_MAP).ifBound(builder::withDetails);
     * 意思是绑定 application 配置文件中 info 开头的配置信息
     * 可以通过 http://host:port/actuator/info 访问到
     */
    @Bean
    public EnvironmentInfoContributor environmentInfoContributor() {
        ConfigurableEnvironment environment = ac.getEnvironment();
        return new EnvironmentInfoContributor(environment);
    }
}
```
