package com.pure.controller;

import com.pure.classloader.SPIClassLoader;
import com.pure.component.SpiLoader;
import com.pure.global.GlobalConstant;
import com.pure.entity.vo.BaseInfoVo;
import com.pure.global.GlobalRef;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.pure.global.GlobalConstant.EXTERNAL_JAR_DIR;
import static com.pure.global.GlobalConstant.JAVA_CLASS_PATH;

/**
 * LoadController
 *
 * @author gnl
 * @since 2023/5/11
 */
@Slf4j
@RestController
@RequestMapping("/load")
public class LoadController {

    private static final String LOCAL_JAR = "./test.jar";

    /**
     * <p>有一点需要注意，需要使用加载外部 JAR 的 ClassLoader 来进行 ServiceLoader.load 操作，
     * <p>这两个步骤需要使用相同的 ClassLoader。
     * <p>并且 JAR 文件上传加载完成之后不可删除，
     */
    private SPIClassLoader cl;

    @Autowired
    private SpiLoader spiLoader;

    @Autowired
    private ConfigurableApplicationContext ac;

    @GetMapping("/local")
    public ResponseEntity<Object> localExt() throws MalformedURLException {
        File externalJar = new File(LOCAL_JAR);

        cl = GlobalRef.pluginClassLoader;
        if (Objects.isNull(cl)) {
            cl = new SPIClassLoader(new URL[]{}, getClass().getClassLoader());
            GlobalRef.setGlobalClassloader(cl);
        }
        cl.loadExternalJar(externalJar);

        log.info("Local jar loaded");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/localExec")
    public ResponseEntity<Object> localLoad() {
        cl = GlobalRef.pluginClassLoader;

        Assert.notNull(cl, "SPI ClassLoader must not be null");

        spiLoader.load(cl);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/external")
    public ResponseEntity<BaseInfoVo> external(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        Assert.notNull(multipartFile, "Upload file is null");

        String fileName = multipartFile.getOriginalFilename();

        Path pluginPath = Paths.get(EXTERNAL_JAR_DIR);
        if (!Files.exists(pluginPath)) {
            Files.createDirectories(pluginPath);
        }

        String filePath = EXTERNAL_JAR_DIR + fileName;
        File externalJar = new File(filePath);

        if (externalJar.exists()) {
            return new ResponseEntity<>(null, HttpStatus.ALREADY_REPORTED);
        }

        try (FileOutputStream fos = new FileOutputStream(externalJar))
        {
            byte[] bytes = multipartFile.getBytes();
            fos.write(bytes);

            cl = GlobalRef.pluginClassLoader;
            if (Objects.isNull(cl)) {
                cl = new SPIClassLoader(new URL[]{}, getClass().getClassLoader());
                GlobalRef.setGlobalClassloader(cl);
            }
            cl.loadExternalJar(externalJar);

            log.info("External jar loaded");

//            log.info("Loading module info");
//            BaseInfoVo vo = loadModuleInfo();

            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BaseInfoVo loadModuleInfo() {
        return null;
    }

    @GetMapping("/exec")
    public void load() {
        cl = GlobalRef.pluginClassLoader;

        Assert.notNull(cl, "SPI ClassLoader must not be null");
        spiLoader.load(cl);
    }

    @GetMapping("/loaded")
    public ResponseEntity<List<String>> loaded() throws IOException {
        Path pluginPath = Paths.get(EXTERNAL_JAR_DIR);
        if (Files.isDirectory(pluginPath)) {
            File pluginDirectory = new File(pluginPath.toUri());
            String[] files = pluginDirectory.list();
            List<String> list = Arrays.asList(files);
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/classpath")
    public void listClasspath() {
        Object o = ac.getEnvironment().getSystemProperties().get(JAVA_CLASS_PATH);
        System.out.println(o);
    }

}
