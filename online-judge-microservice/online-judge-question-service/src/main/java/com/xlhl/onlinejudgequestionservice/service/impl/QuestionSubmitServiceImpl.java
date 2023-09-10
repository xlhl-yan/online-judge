package com.xlhl.onlinejudgequestionservice.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xlhl.onlinejudgecommon.common.ErrorCode;
import com.xlhl.onlinejudgecommon.constant.CommonConstant;
import com.xlhl.onlinejudgecommon.exception.BusinessException;
import com.xlhl.onlinejudgecommon.utils.SqlUtils;
import com.xlhl.onlinejudgemodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.xlhl.onlinejudgemodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.xlhl.onlinejudgemodel.model.entity.Question;
import com.xlhl.onlinejudgemodel.model.entity.QuestionSubmit;
import com.xlhl.onlinejudgemodel.model.entity.User;
import com.xlhl.onlinejudgemodel.model.enums.QuestionSubmitLanguageEnum;
import com.xlhl.onlinejudgemodel.model.enums.QuestionSubmitStatusEnum;
import com.xlhl.onlinejudgemodel.model.vo.QuestionSubmitVO;
import com.xlhl.onlinejudgequestionservice.mapper.QuestionSubmitMapper;
import com.xlhl.onlinejudgequestionservice.rabbitmq.MessageProducer;
import com.xlhl.onlinejudgequestionservice.service.QuestionService;
import com.xlhl.onlinejudgequestionservice.service.QuestionSubmitService;
import com.xlhl.onlinejudgeserviceclient.service.JudgeFeignClient;
import com.xlhl.onlinejudgeserviceclient.service.UserFeignClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author xlhl
 * @description 针对表【question_submit(题目提交)】的数据库操作Service实现
 * @createDate 2023-09-02 20:56:49
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {
    @Resource
    private QuestionService questionService;

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    @Lazy
    private JudgeFeignClient judgeFeignClient;

    @Resource
    private MessageProducer messageProducer;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        // 判断实体是否存在，根据类别获取实体
        Long questionId = questionSubmitAddRequest.getQuestionId();
        String language = questionSubmitAddRequest.getLanguage();
        String code = questionSubmitAddRequest.getCode();

        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (StringUtils.isAnyBlank(language, code)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已提交题目
        long userId = loginUser.getId();
        // 每个用户串行提交题目
        // 锁必须要包裹住事务方法

        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getLanguageEnum(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "无法支持的编程语言");
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setLanguage(language);
        questionSubmit.setCode(code);
        //  设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        //  设置初始信息
        questionSubmit.setJudgeInfo("{}");
        boolean result = this.save(questionSubmit);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }

        //  往消息队列中发送消息
        messageProducer.sendMessage(CommonConstant.EXCHANGE_NAME, CommonConstant.ROUTING_KEY, String.valueOf(questionSubmit.getId()));
//        CompletableFuture.runAsync(() -> {
//
//            judgeFeignClient.doJudge(questionSubmit.getId());
//        });

        return questionSubmit.getId();
    }


    /**
     * 获取查询包装类，根据哪些字段查询，歌女前端传来的，得到 mybatis 框架自动支持的查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {

        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        //  脱敏 仅本人和管理员可见自己（userId != loginUserId）提交的代码
        if (!Objects.equals(questionSubmitVO.getUserId(), loginUser.getId()) && userFeignClient.isAdmin(loginUser)) {
            //  数据脱敏
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionList)) {
            return questionVOPage;
        }
        List<QuestionSubmitVO> submitVOList = questionList.stream().map(questionSubmit -> {
            return getQuestionSubmitVO(questionSubmit, loginUser);
        }).collect(Collectors.toList());
        questionVOPage.setRecords(submitVOList);
        return questionVOPage;
    }
}




