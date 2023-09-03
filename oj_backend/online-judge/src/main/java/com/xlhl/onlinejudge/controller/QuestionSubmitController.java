package com.xlhl.onlinejudge.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlhl.onlinejudge.common.BaseResponse;
import com.xlhl.onlinejudge.common.ErrorCode;
import com.xlhl.onlinejudge.common.ResultUtils;
import com.xlhl.onlinejudge.exception.BusinessException;
import com.xlhl.onlinejudge.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.xlhl.onlinejudge.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.xlhl.onlinejudge.model.entity.QuestionSubmit;
import com.xlhl.onlinejudge.model.entity.User;
import com.xlhl.onlinejudge.model.vo.QuestionSubmitVO;
import com.xlhl.onlinejudge.service.QuestionSubmitService;
import com.xlhl.onlinejudge.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 * @author <a href="https://github.com/xlhl-yan">xlhl</a>
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return resultNum 本次题目提交变化数
     */
    @PostMapping("/")
    public BaseResponse<Long> doSubmitQuestion(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                               HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String language = questionSubmitAddRequest.getLanguage();
        String code = questionSubmitAddRequest.getCode();

        if (StringUtils.isAnyBlank(language, code)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能题目提交
        final User loginUser = userService.getLoginUser(request);
        Long result = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 分页获取题目提交列表，仅管理员和提交本人可以代码与答案
     *
     * @param questionSubmitQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
                                                                         HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();

        Page<QuestionSubmit> userPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(userPage, loginUser));
    }
}
