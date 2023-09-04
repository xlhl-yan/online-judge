package com.xlhl.onlinejudge.judge.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ExecuteCodeResponse
 *
 * @author xlhl
 * @version 1.0
 * @description 代码沙箱请求类
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeRequest {
    /**
     * 输入用例
     */
    private List<String> inputList;

    /**
     * 代码
     */
    private String code;

    /**
     * 编程语言
     */
    private String language;
}
