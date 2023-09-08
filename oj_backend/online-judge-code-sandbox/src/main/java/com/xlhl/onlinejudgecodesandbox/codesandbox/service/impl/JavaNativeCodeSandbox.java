package com.xlhl.onlinejudgecodesandbox.codesandbox.template;

import com.xlhl.onlinejudgecodesandbox.codesandbox.model.ExecuteCodeRequest;
import com.xlhl.onlinejudgecodesandbox.codesandbox.model.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * JavaNativeCodeSandbox
 * Java原生实现
 *
 * @author xlhl
 * @version 1.0
 * @description
 */
@Slf4j
@Component
public class JavaNativeCodeSandbox extends BaseJavaCodeSandboxTemplate {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }
}
