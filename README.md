# pure-pltf

## 介绍
一个 PaaS 平台

<br>

适用场景：
* 电商之类以微服务为主的分布式系统不适用，因为微服务扩展起来也方便，需要什么功能直接前端加页面，后端加服务即可；
* PaaS 平台则不一样，可以在 core 的基础上，根据需求方的要求，进行定制化开发。（NM 又回到定制化）。

<br>

**构成**
- pure-app-core 核心功能
- pure-app-lite 最小实现 Lite 版本
- ...

<br>

**What to do**

实现可拔插式子项目安装。前期支持功能有：共享白版、Web 界面聊天、语音通话、视频通话。

<br>

## 开发历程
> **Update-1**
> <br>
> 后端：
> 实现起来应该没什么难度，主要用到了 Java 的 SPI 机制，只要暴露好借口，提供给外部实现即可。
> 共享版白、Web 聊天和通话这几个功能都需要使用到 Websocket。 Websocket 作为一个外部协议，引入 core 包不合适，哪个功能需要就从在该功能项目直接引入即可。
> core 可能需要一个信息收集接口，由子项目实现，可以直接从 pom.xml 获取基础信息。
> 
> 前端：
> 可以从前端页面选择加载并安装对应的功能


> **Update-2**
> <br>
> 第一步，先做一个全平台的信息收集接口，可以全自动收集当前已加载的应用信息，以及收集安装的应用信息。
> 
> *注意：开发过程中不要过度设计*

> **Update-3**
> 之前的项目中用到 spring-boot-actuator 的时候，在配置文件中添加 info 就可以在 /actuator/info 中读取到，当前项目中用到的 springboot 版本是 2.7.11，直接添加未生效。
> 需要手动注入一个 EnvironmentInfoContributor
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
所以需要判断：如果存在 management.server.port 则使用，否则使用 server.port 否则 使用默认端口号 8080。
要完成判断有两种方式：spEL 或在代码获取配置值然后判断。

<br>

使用 spEL
```java
// spEL
@Value("#{${management.server.port} ?: ${server.port} ?: 8080}")
private String actuatorPort;
```
这个方法的缺点是必须在配置文件中存在 management.server.port 和 server.port 这两个配置，可以不设置值，但是必须存在，否则启动项目的时候就会报错找不到这两个 key。
有一个解决办法是配置文件加上
```properties
spring.config.failOnMissingProperties=false
```
这样就可以关闭 Spring Boot 在启动时对配置项的检查，即使在代码中使用了未定义的配置项也不会报错了。不过需要注意的是，关闭这个参数可能会导致配置出现错误而不被发现，
从而可能会给应用程序带来不必要的风险。建议在开发环境中使用，生产环境尽量不要关闭。

<br>
在代码获取配置

```java
// 如果配置文件中不存在该 key 返回 null，不会报错
String actuatorPort = ac.getEnvironment().getProperty("management.server.port");
```

## 踩坑
### 项目打包
项目使用 maven 打包后使用 java -jar 运行时提示没有主清单属性，检查发现 spring-boot-maven-plugin 已经添加。SpringBoot 官方文档中 [Create an Executable JAR with Maven](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto.build.create-an-executable-jar-with-maven) 有提到：
如果项目继承了 spring-boot-starter-parent，可以直接添加 spring-boot-maven-plugin 并使用
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

如果未使用 spring-boot-starter-parent 仍然可以使用这个插件，但是需要额外添加一个 `<executions>` 标签。 

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <executions>
        <execution>
            <goals>
                <goal>repackage</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

项目现在没有继承自 spring-boot-starter-parent，所以采用第二个方法。

<br>

打包成功后项目启动无问题，但是访问 pure-pltf-core 中定义的 controller 不生效。跟在 idea 中开发时的行为不一致 =_=

目前的项目结构如下
```
pure-pltf
├── pure-pltf-core
├── pure-pltf-lite
```

其中 pure-pltf 用作版本锁定
```xml
<!-- pure-pltf pom.xml -->
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>2.7.11</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

pure-pltf-core 继承自 pure-pltf

```xml
<!-- pure-pltf-core pom.xml -->
<parent>
    <groupId>com.pure</groupId>
    <artifactId>pure-pltf</artifactId>
    <version>1.0-SNAPSHOT</version>
</parent>
```

pure-pltf-lite 继承自 pure-pltf，并依赖 pure-pltf-core
```xml
<!-- pure-pltf-lite pom.xml -->
<parent>
    <groupId>com.pure</groupId>
    <artifactId>pure-pltf</artifactId>
    <version>1.0-SNAPSHOT</version>
</parent>

<dependencies>
    <dependency>
        <groupId>com.pure</groupId>
        <artifactId>pure-pltf-core</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

随后改用 spring-boot-starter-parent，项目结构不变
```
pure-pltf
├── pure-pltf-core
├── pure-pltf-lite
```

pure-pltf-core 和 pure-pltf-lite 不再依赖 pure-pltf，直接依赖 spring-boot-starter-parent：

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.7.11</version>
</parent>
```

*注意：打包运行的时候要先将 pure-pltf-core 安装到 maven 仓库，再打包 pure-pltf-lite*


<br>

### SPI 实现动态加载

#### ServiceLoader 加载外部 jar

需要设置当前线程的上下文类加载器，加载完成后将上下文类加载器设置回原来的值，以避免影响其他模块的加载

```java
public void load(ClassLoader cl) {
    Assert.notNull(cl, "spi classloader must not be null");

    // 设置当前线程的上下文类加载器
    Thread.currentThread().setContextClassLoader(cl);
    doServiceLoad();

    // 加载完成后将上下文类加载器设置回原来的值，以避免影响其他模块的加载
    Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
}

public void doServiceLoad() {
    ServiceLoader<BootSpi> services = ServiceLoader.load(BootSpi.class);
    for (BootSpi service : services) {
        service.load();
    }
}
```

### Java -cp 和 -jar 命令

假如存在一个 SpringBoot 应用 application.jar，以下两条命令运行效果相同
```shell
java -jar application.jar
java -cp application.jar org.springframework.boot.loader.JarLauncher
```

<br>

`java -jar` 执行一个封装好了的 jar 包。其中有一个 MANIFEST.MF 文件
```manifest
Manifest-Version: 1.0
Created-By: Maven JAR Plugin 3.3.0
Build-Jdk-Spec: 17
Implementation-Title: boot-pkg-lite
Implementation-Version: 1.0-SNAPSHOT
Main-Class: org.springframework.boot.loader.JarLauncher
Start-Class: com.demo.lite.LiteMain
Spring-Boot-Version: 3.0.6
Spring-Boot-Classes: BOOT-INF/classes/
Spring-Boot-Lib: BOOT-INF/lib/
Spring-Boot-Classpath-Index: BOOT-INF/classpath.idx
Spring-Boot-Layers-Index: BOOT-INF/layers.idx
```

Main-Class 的一行，指明了含有 `public static void main(String[] args)` 方法的类，作为应用程序的主启动类。
使用 -jar 时，运行的 JAR 文件的 classpath 是系统环境变量中的 CLASSPATH，其他的类路径设置将被忽略。
如果使用 `java -cp <your_classpath> -jar application.jar` 来运行程序的话，其中 `-cp` 部分指定的参数是无效的。

<br>

`java -cp or -classpath or --class-path <your_classpath>` 效果相同。指定 classpath 会覆盖 CLASSPATH 环境变量。
如果没有使用类路径选项，也没有设置 classpath，那么用户的类路径由当前目录（.）组成。

如果需要加载多个 JAR 文件，可以使用 `:` 来作为分隔符（Linux/Unix），Windows 使用 `;`

可以使用 `*` 来加载某路径下所有 JAR 文件 `java -cp "./lib/*" <main-class>`
