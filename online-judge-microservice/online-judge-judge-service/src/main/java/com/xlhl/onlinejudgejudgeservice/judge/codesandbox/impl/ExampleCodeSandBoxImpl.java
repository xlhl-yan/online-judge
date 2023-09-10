package com.xlhl.onlinejudgejudgeservice.judge.codesandbox.impl;


import com.xlhl.onlinejudgejudgeservice.judge.codesandbox.CodeSandBox;
import com.xlhl.onlinejudgemodel.model.codesandbox.ExecuteCodeRequest;
import com.xlhl.onlinejudgemodel.model.codesandbox.ExecuteCodeResponse;
import com.xlhl.onlinejudgemodel.model.codesandbox.JudgeInfo;
import com.xlhl.onlinejudgemodel.model.enums.JudgeInfoMessageEnum;
import com.xlhl.onlinejudgemodel.model.enums.QuestionSubmitStatusEnum;

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
