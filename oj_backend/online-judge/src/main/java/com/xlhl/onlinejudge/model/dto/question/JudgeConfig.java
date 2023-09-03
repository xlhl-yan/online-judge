package com.xlhl.onlinejudge.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * JudgeConfig
 *
 * @author xlhl
 * @version 1.0
 * @description 题目配置
 */
@Data
public class JudgeConfig implements Serializable {
    private static final long serialVersionUID = 9056697568950477080L;

    /**
     * 时间限制 /ms
     */
    private Long timeLimit;

    /**
     * 内存限制 /KB
     */
    private Long memoryLimit;

    /**
     * 空间限制 /KB
     */
    private Long stackLimit;
}
