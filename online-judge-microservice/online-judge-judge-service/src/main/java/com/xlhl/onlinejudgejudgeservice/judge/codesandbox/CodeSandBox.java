package com.xlhl.onlinejudgejudgeservice.judge.codesandbox;


import com.xlhl.onlinejudgemodel.model.codesandbox.ExecuteCodeRequest;
import com.xlhl.onlinejudgemodel.model.codesandbox.ExecuteCodeResponse;

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
