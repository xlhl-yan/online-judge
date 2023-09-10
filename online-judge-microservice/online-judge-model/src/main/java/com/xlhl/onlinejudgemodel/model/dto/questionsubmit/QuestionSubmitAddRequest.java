package com.xlhl.onlinejudgemodel.model.dto.questionsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目点赞请求
 *
 * @author xlhl
 * 
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;


    private static final long serialVersionUID = -3518761816978548363L;
}