package com.xlhl.onlinejudge.judge.codesandbox;

import com.xlhl.onlinejudge.judge.codesandbox.model.ExecuteCodeRequest;
import com.xlhl.onlinejudge.judge.codesandbox.model.ExecuteCodeResponse;
import com.xlhl.onlinejudge.judge.codesandbox.model.enums.CodeSandBoxTypeEnum;
import com.xlhl.onlinejudge.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class CodeSandBoxTest {

    @Value("${codesandbox.type}")
    private String type;

    @Test
    void executeCode() {
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code("int main(){}")
                .language(QuestionSubmitLanguageEnum.JAVA.getLanguage())
                .inputList(Arrays.asList("1,2", "3,4"))
                .build();

        CodeSandBox sandBox = CodeSandBoxFactory.newInstance(CodeSandBoxTypeEnum.EXAMPLE);
        ExecuteCodeResponse executeCodeResponse = sandBox.executeCode(executeCodeRequest);
        assert executeCodeResponse == null;

        sandBox = CodeSandBoxFactory.newInstance(CodeSandBoxTypeEnum.REMOTE);
        executeCodeResponse = sandBox.executeCode(executeCodeRequest);
        assert executeCodeResponse == null;

        sandBox = CodeSandBoxFactory.newInstance(CodeSandBoxTypeEnum.THIRD_PARTY);
        executeCodeResponse = sandBox.executeCode(executeCodeRequest);
        assert executeCodeResponse == null;
    }

    @Test
    void executeCodeByValue() {
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code("int main(){}")
                .language(QuestionSubmitLanguageEnum.JAVA.getLanguage())
                .inputList(Arrays.asList("1,2", "3,4"))
                .build();

        CodeSandBox sandBox = CodeSandBoxFactory.newInstance(type);
        CodeSandBoxProxy sandBoxProxy = new CodeSandBoxProxy(sandBox);
        
        ExecuteCodeResponse executeCodeResponse = sandBoxProxy.executeCode(executeCodeRequest);
        assert executeCodeResponse != null;
    }
}