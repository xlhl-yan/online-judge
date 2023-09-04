package com.xlhl.onlinejudge.judge.codesandbox;

import com.xlhl.onlinejudge.judge.codesandbox.model.ExecuteCodeRequest;
import com.xlhl.onlinejudge.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * CodeSendBoxProxy
 *
 * @author xlhl
 * @version 1.0
 * @description 代码沙箱代理类
 */
@Slf4j
@AllArgsConstructor
public class CodeSandBoxProxy implements CodeSandBox {

    private final CodeSandBox codeSandBox;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求参数：{}", executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);
        log.info("代码沙箱响应日志：{}", executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
