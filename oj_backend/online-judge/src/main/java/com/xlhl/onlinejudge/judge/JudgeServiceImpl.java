package com.xlhl.onlinejudge.judge;

import cn.hutool.json.JSONUtil;
import com.xlhl.onlinejudge.common.ErrorCode;
import com.xlhl.onlinejudge.exception.BusinessException;
import com.xlhl.onlinejudge.exception.ThrowUtils;
import com.xlhl.onlinejudge.judge.codesandbox.CodeSandBox;
import com.xlhl.onlinejudge.judge.codesandbox.CodeSandBoxFactory;
import com.xlhl.onlinejudge.judge.codesandbox.CodeSandBoxProxy;
import com.xlhl.onlinejudge.judge.codesandbox.model.ExecuteCodeRequest;
import com.xlhl.onlinejudge.judge.codesandbox.model.ExecuteCodeResponse;
import com.xlhl.onlinejudge.judge.strategy.JudgeContext;
import com.xlhl.onlinejudge.judge.strategy.JudgeManager;
import com.xlhl.onlinejudge.model.dto.question.JudgeCase;
import com.xlhl.onlinejudge.model.dto.questionsubmit.JudgeInfo;
import com.xlhl.onlinejudge.model.entity.Question;
import com.xlhl.onlinejudge.model.entity.QuestionSubmit;
import com.xlhl.onlinejudge.model.enums.QuestionSubmitStatusEnum;
import com.xlhl.onlinejudge.service.QuestionService;
import com.xlhl.onlinejudge.service.QuestionSubmitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * JudgeServiceImpl
 *
 * @author xlhl
 * @version 1.0
 * @description
 */
@Service
@Slf4j
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private JudgeManager judgeManager;

    @Value("${codesandbox.type}")
    private String type;

    @Override
    public QuestionSubmit doJudge(Long questionSubmitId) {
//        1. 传入题目提交 id，获取到对应题目信息（代码，语言等）
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        ThrowUtils.throwIf(questionSubmit == null, ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        Long questionId = questionSubmit.getQuestionId();

        Question question = questionService.getById(questionId);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");

        Integer status = questionSubmit.getStatus();
        //  判断题目的提交状态；如果不为等待状态，直接返回
        if (!Objects.equals(status, QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }
        //  修改提交状态为判题中
        QuestionSubmit updateQuestionSubmit = new QuestionSubmit();
        updateQuestionSubmit.setId(questionSubmitId);
        updateQuestionSubmit.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionSubmitService.updateById(updateQuestionSubmit);
        if (!update) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目状态无法更新");
        }

        //  调用代码沙箱，获取执行结果
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        String judgeCase = question.getJudgeCase();
        //  获取输入用例
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCase, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());

        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        CodeSandBox sandBox = CodeSandBoxFactory.newInstance(type);
        CodeSandBoxProxy sandBoxProxy = new CodeSandBoxProxy(sandBox);

        ExecuteCodeResponse executeCodeResponse = sandBoxProxy.executeCode(executeCodeRequest);

        //  ● 先判断沙箱执行的结果输出数量与输入用例的数量是否相等
        List<String> outputList = judgeCaseList.stream().map(JudgeCase::getOutput).collect(Collectors.toList());
//        List<String> outputList = executeCodeResponse.getOutputList();


        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setQuestion(question);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestionSubmit(questionSubmit);

        JudgeInfo info = judgeManager.doJudge(judgeContext);

        //  修改数据库中的判题结果
        updateQuestionSubmit = new QuestionSubmit();
        updateQuestionSubmit.setId(questionSubmitId);
        updateQuestionSubmit.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        updateQuestionSubmit.setJudgeInfo(JSONUtil.toJsonStr(info));
        update = questionSubmitService.updateById(updateQuestionSubmit);
        if (!update) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目状态无法更新");
        }

        return questionSubmitService.getById(questionId);
    }
}
