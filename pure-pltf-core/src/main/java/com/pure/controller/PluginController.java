package com.pure.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * TODO: PluginController
 *
 * @author gnl
 * @since 2023/5/11
 */
@Slf4j
@RestController
public class PluginController {

    @GetMapping("/jars")
    public Map<String, Object> jars() {
        return null;
    }

    @GetMapping("/plugins")
    public Map<String, Object> plugins() {
        return null;
    }

    @GetMapping("/install")
    public String install(@RequestParam String pluginName) {
        return null;
    }

}
