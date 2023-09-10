package com.xlhl.onlinejudgejudgeservice.judge.strategy;


import com.xlhl.onlinejudgemodel.model.codesandbox.JudgeInfo;
import com.xlhl.onlinejudgemodel.model.dto.question.JudgeCase;
import com.xlhl.onlinejudgemodel.model.entity.Question;
import com.xlhl.onlinejudgemodel.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * JudgeContext
 * 用于定义在策略中传递的参数
 *
 * @author xlhl
 * @version 1.0
 * @description 判题上下文
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private Question question;

    private List<JudgeCase> judgeCaseList;

    private QuestionSubmit questionSubmit;
}
