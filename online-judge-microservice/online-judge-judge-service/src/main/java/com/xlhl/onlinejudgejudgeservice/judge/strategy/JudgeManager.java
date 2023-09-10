package com.xlhl.onlinejudgejudgeservice.judge.strategy;


import com.xlhl.onlinejudgemodel.model.codesandbox.JudgeInfo;
import com.xlhl.onlinejudgemodel.model.entity.QuestionSubmit;
import com.xlhl.onlinejudgemodel.model.enums.QuestionSubmitLanguageEnum;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * JudgeManage
 * 判题管理，简化调用
 *
 * @author xlhl
 * @version 1.0
 * @description 判题管理，简化调用
 */
@Component
public class JudgeManager {
    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategyImpl();
        if (Objects.equals(QuestionSubmitLanguageEnum.JAVA.getLanguage(), language)) {
            judgeStrategy = new JavaLanguageJudgeStrategyImpl();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
