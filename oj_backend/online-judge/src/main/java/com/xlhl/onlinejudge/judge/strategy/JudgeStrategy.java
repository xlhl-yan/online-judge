package com.xlhl.onlinejudge.judge.strategy;

import com.xlhl.onlinejudge.model.dto.questionsubmit.JudgeInfo;

/**
 * 判题策略
 *
 * @author xlhl
 */
public interface JudgeStrategy {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext);
}
