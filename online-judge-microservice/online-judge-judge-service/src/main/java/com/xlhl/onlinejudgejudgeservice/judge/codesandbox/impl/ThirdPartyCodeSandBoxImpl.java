package com.xlhl.onlinejudgejudgeservice.judge.codesandbox.impl;


import com.xlhl.onlinejudgejudgeservice.judge.codesandbox.CodeSandBox;
import com.xlhl.onlinejudgemodel.model.codesandbox.ExecuteCodeRequest;
import com.xlhl.onlinejudgemodel.model.codesandbox.ExecuteCodeResponse;

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
