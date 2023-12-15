# 开发历程

## Skills

### Object to Map

1、反射获取 Fields 再 put 到 Map 中

2、Object ==> JSON ==> Map （推荐）

...

---

<br>

## 踩坑
### 项目打包

> 项目使用 maven 打包后使用 `java -jar` 运行时提示没有主清单属性。

检查发现 spring-boot-maven-plugin 已经添加。SpringBoot 官方文档中 [Create an Executable JAR with Maven](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto.build.create-an-executable-jar-with-maven) 有提到：
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

...

> **出错的原因**：当前项目使用了 spring-boot-starter-dependencies 而不是继承自 spring-boot-starter-parent，所以需要添加 executions。

...

---

### 项目运行

> 项目启动后访问 pure-pltf-core 中定义的 controller 不生效。跟在 idea 中开发时的行为不一致 =_=

目前的项目结构如下
```
pure-pltf
├── pure-pltf-core
├── pure-pltf-lite
```

其中 pure-pltf 依赖如下
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
```shell
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

> *注意：打包运行的时候要先将 pure-pltf-core 安装到 maven 仓库，再打包 pure-pltf-lite*

...

---

### 配置文件自定义 Info

> 之前的项目中用到 spring-boot-actuator 的时候，在配置文件中添加 info 就可以在 `host:port/actuator/info` 中读取到自定义的 info 信息。
当前项目中用到的 springboot 版本是 2.7.11，直接添加 info 未生效。

...
 
需要手动注入一个 EnvironmentInfoContributor

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

...

---

### 代码获取 spring-boot-start-actuator 信息

有两种办法获取 actuator 信息：

* RestTemplate

* 注入 HealthEndpoint 和 InfoEndpoint

...

使用 RestTemplate 需要用到项目端口号，actuator 的端口号 management.server.port 不一定就是 server.port。

所以需要判断：如果存在 management.server.port 则使用该端口，否则使用 server.port。

...

要完成判断有两种方式：
* 使用 spEL 判断
* 在代码获取配置值然后判断。

<br>

使用 spEL
```java
// spEL
@Value("#{${management.server.port} ?: ${server.port} ?: 8080}")
private String actuatorPort;
```

...

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

...

---

### JAR 包的动态加载与卸载

...

> 两种方法：
> 1、使用自定义的 ClassLoader
> 2、使用 java 命令

...

#### 自定义 ClassLoader 加载外部 jar

**实现细节**

> 详情参见 [boot-pkg](https://github.com/gnl00/boot-pkg)

...

**运行细节**

需要访问动态加载进来的 Jar 包中的类时：需要设置当前线程的上下文 ClassLoader 为【动态加载了该 Jar 的 ClassLoader】。
并且加载完成后将上下文类加载器设置回原来的值，以避免影响其他模块的加载。

```java
public void load(ClassLoader cl) {
    Assert.notNull(cl, "classloader must not be null");

    // 设置当前线程的上下文类加载器
    Thread.currentThread().setContextClassLoader(cl);
    doServiceLoad();

    // 加载完成后将上下文类加载器设置回原来的值，以避免影响其他模块的加载
    Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
}

public void doServiceLoad() {
    ServiceLoader<BootSpi> services = ServiceLoader.load(BootSpi.class);
    for (BootSpi serviceImpl : services) {
        serviceImpl.load(); // 执行子类的对应方法
    }
}
```

#### java -cp 和 -jar 命令

假如存在一个 SpringBoot 应用打包成的 application.jar，以下两条命令运行效果相同

```shell
java -jar application.jar # 1
java -cp application.jar org.springframework.boot.loader.JarLauncher # 2
```
...

打包好的 jar 会有一个 `MANIFEST.MF` 文件，文件内容大概如下

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

...

Main-Class 指明应用程序的主启动类，即含有 `public static void main(String[] args)` 方法的类。

`java -jar` 可以执行一个封装好的 jar 包。

使用 `-jar` 时，运行的 Java 应用的 CLASSPATH 是系统环境变量中的 CLASSPATH，其他的类路径设置将被忽略。

例如，如果使用 `java -cp <your_classpath> -jar application.jar` 来运行程序的话，其中 `-cp` 部分指定的参数是无效的。

...

```shell
java -cp <your_classpath>
java -classpath <your_classpath>
java --class-path <your_classpath>`
```

上面这几条命令执行的效果是相同的。指定的 classpath 会覆盖系统的 CLASSPATH 环境变量。
如果没有使用 classpath 参数，也没有设置系统 classpath，那么此时的 classpath 就是当前目录 `.`。

...

如果需要加载多个 JAR 文件，Linux/Unix 使用 `:` 来作为分隔符，Windows 使用 `;`。

`java -cp abc.jar:edf.jar:123.jar <main-class>`

此外，还可以使用 `*` 来加载某路径下所有 JAR 文件 `java -cp "./lib/*" <main-class>`，加载 lib 子目录下的所有 JAR 文件。

...

#### 两种方式对比

使用 ServiceLoader 来加载更加灵活，不需要先将 JAR 文件放入 classpath 中，只需要自定义好 ClassLoader，用来动态加载外部 JAR 即可。

`java -cp` 方式需要在项目启动时指定 classpath，后续如果引入外部 JAR 需要先将外部 JAR 文件保存到 classpath，然后再刷新 classpath 才能使用新引入的文件。

本项目使用第一种方法。

#### ServiceLoader.load() 探析

```java
public static <S> ServiceLoader<S> load(Class<S> service) {
    // 获取当前上下文 ClassLoader
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    return new ServiceLoader<>(Reflection.getCallerClass(), service, cl);
}

private ServiceLoader(Class<?> caller, Class<S> svc, ClassLoader cl) {
    // svc => SPI 接口
    // cl => ClassLoader
    Objects.requireNonNull(svc);

    // 如果未指定 ClassLoader 则使用 SystemClassLoader 来加载
    if (VM.isBooted()) {
        checkCaller(caller, svc);
        if (cl == null) {
            cl = ClassLoader.getSystemClassLoader();
        }
    } else {

        // 如果当前处于 VM 初始化时机，只有 java.base 这个包内的代码能执行
        
        // if we get here then it means that ServiceLoader is being used
        // before the VM initialization has completed. At this point then
        // only code in the java.base should be executing.
        Module callerModule = caller.getModule();
        Module base = Object.class.getModule();
        Module svcModule = svc.getModule();
        if (callerModule != base || svcModule != base) {
            fail(svc, "not accessible to " + callerModule + " during VM init");
        }

        // restricted to boot loader during startup
        cl = null;
    }

    this.service = svc;
    this.serviceName = svc.getName();
    this.layer = null;
    this.loader = cl;
    // 返回一个 AccessControlContext
    // AccessControlContext 用于根据它所封装的上下文做出系统资源访问决定
    this.acc = (System.getSecurityManager() != null)
            ? AccessController.getContext()
            : null;
}
```

#### 总结

> 在编写这个功能时第一个想到的是 SPI 使用 ServiceLoader.load 方法，但是刚开始没有自定义 ClassLoader，无法正常加载外部 JAR。
>
> 然后转向另一个方法 `java -cp ./lib/* -jar application.jar` 应用启动没问题，但一开始并不知道 `-cp` 和 `-jar` 的关系。
>
> 使用 Environment 类查看 classpath 发现只有 application.jar，其他的 jar 并未被引入。
>
> 原来指定 `-jar` 之后会忽略 `-cp` 指定的 CLASSPATH。这两个命令使用时只能二选一。
>
> 还有一点是 `java -cp ./lib/*` 需要给 `-cp` 的参数加上双引号 `java -cp "./lib/*"`，否则无法解析这条命令。

...

---

### JAR 包的动态卸载

动态卸载 JAR 实现起来相对动态加载较难，目前想到的方法：
1. 将 JAR 包从 plugins 目录中删除，然后重启应用；
2. 每动态加载一个 JAR 都使用一个单独的 ClassLoader，需要卸载该 JAR 的时候直接让 JVM 回收对应的 ClassLoader

<br>


## 碎碎念

**Update-3**
> 手动注入一个 EnvironmentInfoContributor 读取配置文件中的自定义 info

**Update-2**
> 第一步，先做一个全平台的信息收集接口，可以全自动收集当前已加载的应用信息，以及收集安装的应用信息。
> 一些基础的信息可以从 Environment 类或者直接从 pom.xml 获取。

**Update-1**
> 后端：
> 目前看来难度不大。主要用到了 Java 的 SPI 机制，只要暴露好接口，提供给外部实现即可。
>
> 前端：
> 可以从前端页面选择加载并安装对应的功能
