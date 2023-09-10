package com.xlhl.onlinejudgejudgeservice.judge.strategy;


import com.xlhl.onlinejudgemodel.model.codesandbox.JudgeInfo;

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
