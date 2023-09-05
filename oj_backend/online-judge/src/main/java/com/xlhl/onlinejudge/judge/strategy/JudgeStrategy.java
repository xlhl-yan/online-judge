package com.xlhl.onlinejudge.judge.strategy;

import com.xlhl.onlinejudge.judge.codesandbox.model.JudgeInfo;

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
