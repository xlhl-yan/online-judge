package com.xlhl.onlinejudgecodesandbox.codesandbox.service;


import com.xlhl.onlinejudgecodesandbox.codesandbox.model.ExecuteCodeRequest;
import com.xlhl.onlinejudgecodesandbox.codesandbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 *
 * @author xlhl
 */
public interface CodeSandBox {
    /**
     * 调用代码沙箱执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
