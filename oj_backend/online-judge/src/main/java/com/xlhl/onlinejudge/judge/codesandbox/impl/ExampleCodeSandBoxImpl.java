package com.xlhl.onlinejudge.judge.codesandbox.impl;

import com.xlhl.onlinejudge.judge.codesandbox.CodeSandBox;
import com.xlhl.onlinejudge.judge.codesandbox.model.ExecuteCodeRequest;
import com.xlhl.onlinejudge.judge.codesandbox.model.ExecuteCodeResponse;
import com.xlhl.onlinejudge.judge.codesandbox.model.JudgeInfo;
import com.xlhl.onlinejudge.model.enums.JudgeInfoMessageEnum;
import com.xlhl.onlinejudge.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * ExampleCodeSandBoxImpl
 *
 * @author xlhl
 * @version 1.0
 * @description 实例代码沙箱（测试业务流程用）
 */
public class ExampleCodeSandBoxImpl implements CodeSandBox {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();

        ExecuteCodeResponse response = new ExecuteCodeResponse();

        response.setOutputList(inputList);
        response.setMessage("接口执行成功");
        response.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());

        JudgeInfo info = new JudgeInfo();
        info.setTime(1000L);
        info.setMemory(1000L);
        info.setMessage(JudgeInfoMessageEnum.ACCEPTED.getDescription());

        response.setJudgeInfo(info);

        return response;
    }
}
