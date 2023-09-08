package com.xlhl.onlinejudge.judge;

import com.xlhl.onlinejudge.model.entity.QuestionSubmit;

/**
 * @author xlhl
 */
public interface JudgeService {

    /**
     * 判题服务
     *
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudge(Long questionSubmitId);


}
