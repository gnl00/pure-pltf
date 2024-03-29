package com.pure.controller;

import com.pure.entity.info.vo.AppInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * CoreController
 *
 * @author gnl
 * @date 2023/5/11
 */
@Slf4j
@RestController
public class CoreController {

    /**
     * <p>注意：需要使用加载外部 JAR 的 ClassLoader 来进行 ServiceLoader.load 操作
     * <p>这两个步骤需要使用相同的 ClassLoader
     * <p>并且 JAR 文件上传加载完成之后不可删除
     */


//    @GetMapping("/local")
//    public ResponseEntity<Object> localExt() throws MalformedURLException {
//        File externalJar = new File(LOCAL_JAR);
//
//        cl = GlobalRef.pluginClassLoader;
//        if (Objects.isNull(cl)) {
//            cl = new TestClassLoader(new URL[]{}, getClass().getClassLoader());
//            GlobalRef.setDynamicClassloader(cl);
//        }
//        // cl.loadExternalJar(externalJar);
//
//        log.info("Local jar loaded");
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

//    @GetMapping("/localExec")
//    public ResponseEntity<Object> localLoad() {
//        cl = GlobalRef.pluginClassLoader;
//
//        Assert.notNull(cl, "ClassLoader must not be null");
//        if (Objects.isNull(testLoader)) {
//            testLoader = new TestLoader();
//        }
//        testLoader.loadOne(cl);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

//    @PostMapping("/external")
//    public ResponseEntity<String> external(@RequestParam("file") MultipartFile multipartFile) throws IOException {
//        Assert.notNull(multipartFile, "Upload file is null");
//
//        String fileName = multipartFile.getOriginalFilename();
//
//        Path pluginPath = Paths.get(EXTERNAL_JAR_DIR);
//        if (!Files.exists(pluginPath)) {
//            Files.createDirectories(pluginPath);
//        }
//
//        String filePath = EXTERNAL_JAR_DIR + fileName;
//        File externalJar = new File(filePath);
//
//        if (externalJar.exists()) {
//            return new ResponseEntity<>("Plugin loaded", HttpStatus.ALREADY_REPORTED);
//        }
//
//        try (FileOutputStream fos = new FileOutputStream(externalJar))
//        {
//            byte[] bytes = multipartFile.getBytes();
//            fos.write(bytes);
//
//            cl = GlobalRef.pluginClassLoader;
//            if (Objects.isNull(cl)) {
//                cl = new TestClassLoader(new URL[]{}, getClass().getClassLoader());
//                GlobalRef.setDynamicClassloader(cl);
//            }
//            // cl.loadExternalJar(externalJar);
//
//            log.info("External jar loaded");
//
////            log.info("Loading module info");
////            BaseInfoVo vo = loadModuleInfo();
//
//            return new ResponseEntity<>("External jar loaded", HttpStatus.OK);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private AppInfoVo loadCustomInfo() {
        return null;
    }

//    @GetMapping("/exec")
//    public void load() {
//        cl = GlobalRef.pluginClassLoader;
//
//        Assert.notNull(cl, "ClassLoader must not be null");
//        if (Objects.isNull(testLoader)) {
//            testLoader = new TestLoader();
//        }
//        testLoader.loadOne(cl);
//    }

}
