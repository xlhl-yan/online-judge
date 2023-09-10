package com.xlhl.onlinejudgejudgeservice.judge;


import com.xlhl.onlinejudgemodel.model.entity.QuestionSubmit;

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
