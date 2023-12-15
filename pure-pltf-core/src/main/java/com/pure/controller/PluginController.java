package com.pure.controller;

import com.pure.base.PluginMetadata;
import com.pure.handler.PluginHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
                String resp = install(pkgName, url);
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
        return install(null, null);
    }

    @PostMapping("/installFromFile")
    public String installFromFile(MultipartFile multipartFile) {
        // TODO parse name
        return install(null, null);
    }

    private String parsePkgName(String pkgPath) {
        return null;
    }

    private String install(String plgName, URL url) {
        int check = 0;
        if ((check = pluginHandler.install(plgName, url)) == -1) {
            return "plugin already exists";
        } else if (check == 0) {
            return "plugin install failed";
        }
        return "plugin install successfully";
    }

    /**
     * 列出所有已安装的插件
     */
    @GetMapping("/list")
    public Map<String, PluginMetadata> plugins() {
        return pluginHandler.listInstalled();
    }

    @GetMapping("/uninstall")
    public String uninstall(@RequestParam String pluginName) {
        return null;
    }


    @GetMapping("/execute")
    public String execute(@RequestParam String pluginName) {
        return pluginHandler.execute(pluginName) == 1 ? "execute successfully" : "execute failed";
    }
}
