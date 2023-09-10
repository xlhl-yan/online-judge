package com.xlhl.onlinejudgejudgeservice.judge.codesandbox;


import com.xlhl.onlinejudgejudgeservice.judge.codesandbox.impl.ExampleCodeSandBoxImpl;
import com.xlhl.onlinejudgejudgeservice.judge.codesandbox.impl.RemoteCodeSandBoxImpl;
import com.xlhl.onlinejudgejudgeservice.judge.codesandbox.impl.ThirdPartyCodeSandBoxImpl;
import com.xlhl.onlinejudgemodel.model.enums.CodeSandBoxTypeEnum;

/**
 * CodeSendBoxFactory
 * 根据字符串参数创建代码沙箱实例
 *
 * @author xlhl
 * @version 1.0
 * @description 代码沙箱创建工厂（根据字符串参数）
 */
public class CodeSandBoxFactory {
    /**
     * 创建代码沙箱实例
     *
     * @param type 沙箱类型
     * @return
     */
    public static CodeSandBox newInstance(String type) {
        switch (type) {
            case "example":
                return new ExampleCodeSandBoxImpl();
            case "remote":
                return new RemoteCodeSandBoxImpl();
            case "third_party":
                return new ThirdPartyCodeSandBoxImpl();
            default:
                return new ExampleCodeSandBoxImpl();
        }
    }

    /**
     * 创建代码沙箱实例
     *
     * @param codeSandBoxTypeEnum 沙箱类型
     * @return
     */
    public static CodeSandBox newInstance(CodeSandBoxTypeEnum codeSandBoxTypeEnum) {
        switch (codeSandBoxTypeEnum) {
            case EXAMPLE:
                return new ExampleCodeSandBoxImpl();
            case REMOTE:
                return new RemoteCodeSandBoxImpl();
            case THIRD_PARTY:
                return new ThirdPartyCodeSandBoxImpl();
            default:
                return new ExampleCodeSandBoxImpl();
        }
    }
}
