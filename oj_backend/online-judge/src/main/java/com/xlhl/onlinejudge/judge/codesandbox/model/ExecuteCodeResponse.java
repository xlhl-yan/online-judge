package com.xlhl.onlinejudge.judge.codesandbox.model;

import com.xlhl.onlinejudge.model.dto.questionsubmit.JudgeInfo;
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
 * @description 代码沙箱响应类
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {
    /**
     * 输入用例
     */
    private List<String> outputList;

    /**
     * 接口信息
     */
    private String message;

    /**
     * 执行状态
     */
    private Integer status;


    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;
}
