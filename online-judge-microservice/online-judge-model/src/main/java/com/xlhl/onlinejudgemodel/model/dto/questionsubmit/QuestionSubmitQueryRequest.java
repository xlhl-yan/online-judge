package com.xlhl.onlinejudgemodel.model.dto.questionsubmit;

import com.xlhl.onlinejudgecommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author <a href="https://github.com/xlhl-yan">xlhl</a>
 * 
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 提交状态
     */
    private Integer status;

    /**
     * 提交人 id
     */
    private Long userId;



    private static final long serialVersionUID = -3518761816978548363L;
}