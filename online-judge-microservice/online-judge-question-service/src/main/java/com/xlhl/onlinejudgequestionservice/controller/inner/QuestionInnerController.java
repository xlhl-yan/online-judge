package com.xlhl.onlinejudgequestionservice.controller.inner;

import com.xlhl.onlinejudgemodel.model.entity.Question;
import com.xlhl.onlinejudgemodel.model.entity.QuestionSubmit;
import com.xlhl.onlinejudgequestionservice.service.QuestionService;
import com.xlhl.onlinejudgequestionservice.service.QuestionSubmitService;
import com.xlhl.onlinejudgeserviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 内部使用的题目服务
 *
 * @author <a href="https://github.com/xlhl-yan">xlhl</a>
 */
@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    /**
     * 根据id获取题目信息
     *
     * @param questionId 题目id
     * @return
     */
    @GetMapping("/get/id")
    @Override
    public Question getById(@RequestParam("questionId") Long questionId) {
        return questionService.getById(questionId);
    }

    /**
     * 根据id获取题目提交信息
     *
     * @param questionSubmitId 题目提交id
     * @return
     */
    @GetMapping("/question_submit/get/id")
    @Override
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") Long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    /**
     * 根据 id 更新题目信息
     *
     * @param questionSubmit
     * @return
     */
    @PostMapping("/question_submit/update")
    @Override
    public Boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }
}
