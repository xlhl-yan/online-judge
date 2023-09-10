package com.xlhl.onlinejudgeserviceclient.service;

import com.xlhl.onlinejudgemodel.model.entity.Question;
import com.xlhl.onlinejudgemodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xlhl
 * @description 针对表【question(题目)】的数据库操作Service
 * @createDate 2023-09-02 20:56:49
 */
@FeignClient(name = "online-judge-question-service", path = "/api/question/inner")
public interface QuestionFeignClient {

    /**
     * 根据id获取题目信息
     *
     * @param questionId 题目id
     * @return
     */
    @GetMapping("/get/id")
    Question getById(@RequestParam("questionId") Long questionId);

    /**
     * 根据id获取题目提交信息
     *
     * @param questionSubmitId 题目提交id
     * @return
     */
    @GetMapping("/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") Long questionSubmitId);

    /**
     * 根据 id 更新题目信息
     *
     * @param questionSubmit
     * @return
     */
    @PostMapping("/question_submit/update")
    Boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);
}
