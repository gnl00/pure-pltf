package com.pure.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LiteController {

    @GetMapping("/lite")
    public String lite() {
        return "lite is running";
    }

}
