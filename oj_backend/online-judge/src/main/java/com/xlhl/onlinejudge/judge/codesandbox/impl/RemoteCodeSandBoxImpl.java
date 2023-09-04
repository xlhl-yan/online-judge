package com.xlhl.onlinejudge.judge.codesandbox.impl;

import com.xlhl.onlinejudge.judge.codesandbox.CodeSandBox;
import com.xlhl.onlinejudge.judge.codesandbox.model.ExecuteCodeRequest;
import com.xlhl.onlinejudge.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * RemoteCodeSandBoxImpl
 *
 * @author xlhl
 * @version 1.0
 * @description 远程代码沙箱（实际调用接口的沙箱）
 */
public class RemoteCodeSandBoxImpl implements CodeSandBox {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        return null;
    }
}
