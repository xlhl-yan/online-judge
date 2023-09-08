package com.xlhl.onlinejudge.judge.codesandbox.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.xlhl.onlinejudge.common.ErrorCode;
import com.xlhl.onlinejudge.exception.BusinessException;
import com.xlhl.onlinejudge.judge.codesandbox.CodeSandBox;
import com.xlhl.onlinejudge.judge.codesandbox.model.ExecuteCodeRequest;
import com.xlhl.onlinejudge.judge.codesandbox.model.ExecuteCodeResponse;
import com.xlhl.onlinejudge.judge.codesandbox.model.JudgeInfo;
import com.xlhl.onlinejudge.model.enums.JudgeInfoMessageEnum;
import org.springframework.http.HttpStatus;

/**
 * RemoteCodeSandBoxImpl
 * 远程代码沙箱（实际调用接口的沙箱）
 *
 * @author xlhl
 * @version 1.0
 * @description
 */
public class RemoteCodeSandBoxImpl implements CodeSandBox {

    /**
     * 保证安全性，定义鉴权请求头
     */
    private static final String AUTH_REQUEST_HEADER = "auth";
    private static final String AUTH_REQUEST_SECRET = "Admin";


    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        String url = "http://127.0.0.1:8082/executeCode";
        HttpResponse httpResponse = HttpUtil.createPost(url)
                .body(JSONUtil.toJsonStr(executeCodeRequest))
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .execute();
        int status = httpResponse.getStatus();
        if (status != HttpStatus.OK.value()) {
            ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
            executeCodeResponse.setStatus(status);
            JudgeInfo judgeInfo = new JudgeInfo();
            judgeInfo.setMessage(JudgeInfoMessageEnum.COMPILE_ERROR.getDescription());
            executeCodeResponse.setJudgeInfo(judgeInfo);
            return executeCodeResponse;
        }
        String body = httpResponse.body();
        if (StrUtil.isBlank(body)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode RemoteCodeSandBox error message=" + body);
        }
        System.out.println(body);
        ExecuteCodeResponse executeCodeResponse = JSONUtil.toBean(body, ExecuteCodeResponse.class);
        return executeCodeResponse;
    }
}
