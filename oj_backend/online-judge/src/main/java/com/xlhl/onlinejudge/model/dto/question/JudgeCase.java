package com.xlhl.onlinejudge.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * judgeCase
 *
 * @author xlhl
 * @version 1.0
 * @description 题目用例
 */
@Data
public class JudgeCase implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 输入用例
     */
    private String input;

    /**
     * 输出用例
     */
    private String output;
}
