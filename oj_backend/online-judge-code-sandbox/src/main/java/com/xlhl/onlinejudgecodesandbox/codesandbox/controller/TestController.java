package com.xlhl.onlinejudgecodesandbox.codesandbox.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController
 *
 * @author xlhl
 * @version 1.0
 * @description 测试用接口
 */
@RequestMapping
@RestController
public class TestController {

    @GetMapping("/hello")
    public String test(){
        return "HelloWorld";
    }
}
