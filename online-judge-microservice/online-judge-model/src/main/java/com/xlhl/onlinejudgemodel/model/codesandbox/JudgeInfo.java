package com.xlhl.onlinejudgemodel.model.codesandbox;

import lombok.Data;

import java.io.Serializable;

/**
 * 判题信息
 *
 * @author <a href="https://github.com/xlhl-yan">xlhl</a>
 */
@Data
public class JudgeInfo implements Serializable {
    private static final long serialVersionUID = 9056697568950477080L;

    /**
     * 消耗时间
     */
    private Long time;

    /**
     * 消耗的内存
     */
    private Long memory;

    /**
     * 程序执行信息
     */
    private String message;
}