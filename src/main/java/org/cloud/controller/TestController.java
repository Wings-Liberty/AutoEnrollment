package org.cloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test(){
        return "test success";
    }

    @GetMapping("/test/token")
    public String testToken(){
        return "token is ok";
    }

}
