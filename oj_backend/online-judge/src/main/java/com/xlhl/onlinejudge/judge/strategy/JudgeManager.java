package com.xlhl.onlinejudge.judge.strategy;

import com.xlhl.onlinejudge.judge.codesandbox.model.JudgeInfo;
import com.xlhl.onlinejudge.model.entity.QuestionSubmit;
import com.xlhl.onlinejudge.model.enums.QuestionSubmitLanguageEnum;
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
