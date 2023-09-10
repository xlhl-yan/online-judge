package com.xlhl.onlinejudgejudgeservice.controller.inner;

import com.xlhl.onlinejudgejudgeservice.judge.JudgeService;
import com.xlhl.onlinejudgemodel.model.entity.QuestionSubmit;
import com.xlhl.onlinejudgeserviceclient.service.JudgeFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 内部使用的判题服务
 *
 * @author xlhl
 */
@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignClient {

    @Resource
    private JudgeService judgeService;

    /**
     * 判题服务
     *
     * @param questionSubmitId
     * @return
     */
    @Override
    @PostMapping("/do")
    public QuestionSubmit doJudge(@RequestParam("questionSubmitId") Long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }
}
