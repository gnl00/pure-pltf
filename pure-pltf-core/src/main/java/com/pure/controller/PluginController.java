package com.pure.controller;

import com.pure.Plugin;
import com.pure.handler.PluginHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * PluginController
 *
 * @author gnl
 * @date 2023/5/11
 */
@Slf4j
@RestController
@RequestMapping("/plugin")
public class PluginController {

    @Autowired
    private ApplicationContext ac;

    @Autowired
    private PluginHandler pluginHandler;

    /**
     * 获取根目录所有 JAR 包
     */
    @GetMapping("/jars")
    public Map<String, Object> jars() {
        return pluginHandler.listRootJars();
    }

    @GetMapping("/installFromRoot")
    public ResponseEntity<String> installFromRoot(@RequestParam String pkgName) {
        if (StringUtils.hasText(pkgName)) {
            try {
                Path pkgPath = Paths.get(pkgName);
                File file = pkgPath.toFile();
                URL url = file.toURI().toURL();
                String resp = install(url);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            } catch (MalformedURLException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("pkgName is empty", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/installFromPath")
    public String installFromPath(@RequestParam String pkgPath) {
        // TODO parse name
        return install(null);
    }

    @PostMapping("/installFromFile")
    public String installFromFile(MultipartFile multipartFile) {
        // TODO parse name
        return install(null);
    }

    private String parsePkgName(String pkgPath) {
        return null;
    }

    private String install(URL url) {
        int check = 0;
        if ((check = pluginHandler.install(url)) == PluginHandler.ALREADY_EXIST) {
            return "plugin already exists";
        } else if (check == PluginHandler.INSTALL_FAILED) {
            return "plugin install failed";
        }
        return "plugin install successfully";
    }

    /**
     * 列出所有已安装的插件
     */
    @GetMapping("/list")
    public Map<String, Plugin> plugins() {
        return pluginHandler.listInstalled();
    }

    @GetMapping("/uninstall")
    public String uninstall(@RequestParam String pluginName) {
        return pluginHandler.uninstall(pluginName) == 1 ? "uninstall successfully" : "uninstall failed";
    }


    @GetMapping("/execute")
    public String execute(@RequestParam String pluginName) {
        return pluginHandler.execute(pluginName) == 1 ? "execute successfully" : "execute failed";
    }

    @GetMapping("/get")
    public void getPlugin(@RequestParam String beanName) {
        System.out.println("+++++ get plugin bean byName +++++");
        Plugin bean = (Plugin) ac.getBean(beanName);
        System.out.println(bean);
    }

    @GetMapping("/test")
    public String test() {
        install(null);
        return "test";
    }
}
