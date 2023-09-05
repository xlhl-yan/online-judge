package com.xlhl.onlinejudgecodesandbox.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ExecuteMessage
 *
 * @author xlhl
 * @version 1.0
 * @description 封装返回信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteMessage {

    /**
     * 程序执行状态码
     */
    private Integer exitValue;

    /**
     * 程序执行输出
     */
    private String message;

    /**
     * 程序执行错误输出
     */
    private String errorMessage;

    /**
     * 执行用时
     */
    private Long timeConsuming;
}
