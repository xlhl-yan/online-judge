package com.xlhl.onlinejudgecodesandbox.codesandbox.controller;

import com.xlhl.onlinejudgecodesandbox.codesandbox.model.ExecuteCodeRequest;
import com.xlhl.onlinejudgecodesandbox.codesandbox.model.ExecuteCodeResponse;
import com.xlhl.onlinejudgecodesandbox.codesandbox.template.JavaNativeCodeSandbox;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * ExecuteCodeController
 *
 * @author xlhl
 * @version 1.0
 * @description
 */
@RequestMapping
@RestController
public class ExecuteCodeController {

    /**
     * 保证安全性，定义鉴权请求头
     */
    private static final String AUTH_REQUEST_HEADER = "auth";
    private static final String AUTH_REQUEST_SECRET = "Admin";


    @Resource
    private JavaNativeCodeSandbox javaNativeCodeSandbox;

    @GetMapping("/hello")
    public String test() {
        return "HelloWorld";
    }

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    @PostMapping("/executeCode")
    public ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {

        //  进行一个基本的认证
        String authHeader = request.getHeader(AUTH_REQUEST_HEADER);
        if (!Objects.equals(AUTH_REQUEST_SECRET, authHeader)) {
            response.setStatus(403);
            return null;
        }
        if (executeCodeRequest == null) {
            throw new NullPointerException("请求参数为空");
        }
        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandbox.executeCode(executeCodeRequest);
        if (executeCodeResponse == null) {
            throw new RuntimeException("获取执行结果失败");
        }
        return executeCodeResponse;
    }

}
