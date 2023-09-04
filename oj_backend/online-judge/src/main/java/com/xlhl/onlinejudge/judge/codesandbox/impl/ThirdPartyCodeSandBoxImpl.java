package com.xlhl.onlinejudge.judge.codesandbox.impl;

import com.xlhl.onlinejudge.judge.codesandbox.CodeSandBox;
import com.xlhl.onlinejudge.judge.codesandbox.model.ExecuteCodeRequest;
import com.xlhl.onlinejudge.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * ThirdPartyCodeSandBoxImpl
 *
 * @author xlhl
 * @version 1.0
 * @description 第三方代码沙箱
 */
public class ThirdPartyCodeSandBoxImpl implements CodeSandBox {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
