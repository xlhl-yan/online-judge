package com.xlhl.onlinejudgeserviceclient.service;


import com.xlhl.onlinejudgemodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xlhl
 */
@FeignClient(name = "online-judge-judge-service", path = "/api/judge/inner")
public interface JudgeFeignClient {

    /**
     * 判题服务
     *
     * @param questionSubmitId
     * @return
     */
    @PostMapping("/do")
    QuestionSubmit doJudge(@RequestParam("questionSubmitId") Long questionSubmitId);
}
