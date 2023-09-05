package com.xlhl.onlinejudgecodesandbox.codesandbox.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.WordTree;
import com.xlhl.onlinejudgecodesandbox.codesandbox.model.ExecuteCodeRequest;
import com.xlhl.onlinejudgecodesandbox.codesandbox.model.ExecuteCodeResponse;
import com.xlhl.onlinejudgecodesandbox.codesandbox.model.ExecuteMessage;
import com.xlhl.onlinejudgecodesandbox.codesandbox.model.JudgeInfo;
import com.xlhl.onlinejudgecodesandbox.codesandbox.service.CodeSandBox;
import com.xlhl.onlinejudgecodesandbox.codesandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * JavaNativeCodeSandBox
 *
 * @author xlhl
 * @version 1.0
 * @description Java原生代码沙箱
 */
@Component
@Slf4j
public class JavaNativeCodeSandBox implements CodeSandBox {

    /**
     * 临时文件的公共包名
     */
    private static final String GLOBAL_CODE_DIR_NAME = "tmpCode";

    /**
     * 临时文件的名称
     */
    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    /**
     * 代码的最大执行时间 5s
     */
    private static final long TIMEOUT = 1000 * 5;

    /**
     * 黑名单，记录了禁止代码哪些操作
     */
    private static final List<String> BLANK_LIST = Arrays.asList("Files", "exec");

    /**
     * 字典树（存储白名单）
     */
    private static final WordTree WORD_TREE;

    /**
     * 自定义 Security Manager 字节码文件所在的绝对路径
     */
    private static final String SECURITY_PATH = "C:\\JavaCode\\online-oj\\oj_backend\\online-judge-code-sandbox\\src\\main\\resources\\security";

    /**
     * 自定义 Security Manager 字节码文件名称
     */
    private static final String SECURITY_CLASS_NAME = "OnlineJudgeSecurityManager";

    static {
        //  初始化字典树
        WORD_TREE = new WordTree();
        WORD_TREE.addWords(BLANK_LIST);
    }

    /**
     * 调用代码沙箱执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
//        System.setSecurityManager(new DenySecurityManager());

        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();
        System.out.println("传入代码为：{\n" + code + "}");
//        log.info("传入代码为：{}", code);
        //  校验代码
//        FoundWord foundWord = WORD_TREE.matchWord(code);
//        if (foundWord != null) {
//            System.out.println("出现敏感词:：{" + foundWord.getFoundWord() + "}");
////            log.info("出现敏感词:{}", foundWord.getFoundWord());
//            return null;
//        }

        //  region 1.把代码写入到临时文件中
        String property = System.getProperty("user.dir");
        String globalCodePathName = property + File.separator + GLOBAL_CODE_DIR_NAME;
        //  判断全局代码目录是否存在;没有则新建
        if (FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }
        //  把用户的代码隔离存放
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        log.info("临时文件：{}", userCodeParentPath);
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
        if (!FileUtil.exist(userCodeFile)) {
            System.out.println("文件不存在:：{" + userCodePath + "}");
        }
        //  endregion

        //  region 2.编译代码得到class文件
        String compileCmd = String.format("javac -encoding UTF-8 %s", userCodeFile.getAbsolutePath());
        log.info("执行命令：{}", compileCmd);
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            //  等待程序执行获取状态码
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            System.out.println(executeMessage);
        } catch (Exception e) {
            return getErrorResponse(e);
        }
        //  endregion

        //  region 3.执行代码得到输出结果
        List<ExecuteMessage> outputList = new ArrayList<>();
        for (String input : inputList) {
            String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s;%s -Djava.security.manager=%s Main %s", userCodeParentPath, SECURITY_PATH, SECURITY_CLASS_NAME, input);

            try {
                Process runProcess = Runtime.getRuntime().exec(runCmd);

                /*
                    开启一个守护线程，线程等待运行用户代码执行的最大时间
                    若线程醒来用户代码未结束
                    则直接杀掉
                */
                CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(TIMEOUT);
                        System.out.println("运行超时");
                        //  判断进程是否完成
                        runProcess.destroy();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
                outputList.add(executeMessage);
//                ExecuteMessage executeMessage = ProcessUtils.runInterProcessAndGetMessage(runProcess, input);
                System.out.println(executeMessage);
            } catch (Exception e) {
                return getErrorResponse(e);
            }

        }

//        endregion

        //  region 4. 收集整理输出结果
        ExecuteCodeResponse response = new ExecuteCodeResponse();
        List<String> outputMessageList = new ArrayList<>();
        long maxTime = 0L;
        for (ExecuteMessage executeMessage : outputList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                response.setMessage(errorMessage);
                //  用户提交的代码执行出现错误
                response.setStatus(3);
                break;
            }
            if (StrUtil.isNotBlank(executeMessage.getMessage())) {
                outputMessageList.add(executeMessage.getMessage());
            }
            //  取最大值判断是否超时
            Long consuming = executeMessage.getTimeConsuming();
            if (consuming != null) {
                maxTime = Math.max(maxTime, consuming);
            }
        }
        if (outputMessageList.size() == outputList.size()) {
            //  正常运行完成
            response.setStatus(1);
        }
        response.setOutputList(outputMessageList);


        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        //  要借助第三方库获取内存占用，非常麻烦

//        judgeInfo.setMemory();

        response.setJudgeInfo(judgeInfo);

        //  endregion

        //  region  5.文件清理
        if (userCodeFile.getParent() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            if (del) {
                log.info("文件删除成功:{}", userCodeParentPath);
            } else {
                log.error("文件删除失败:{}", userCodeParentPath);
            }
        }
        //  endregion

        //  region  6.错误处理，提升程序健壮性


        //  endregion
        return response;
    }

    /**
     * 获取错误的响应
     *
     * @param e
     * @return
     */
    private ExecuteCodeResponse getErrorResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(null);
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());

        return executeCodeResponse;
    }

    public static void main(String[] args) {
        JavaNativeCodeSandBox javaNativeCodeSandBox = new JavaNativeCodeSandBox();
        ExecuteCodeRequest request = new ExecuteCodeRequest();
        request.setInputList(Arrays.asList("1 2", " 1 3"));

//        String code = ResourceUtil.readStr("testCode/simpleComputeArgs/Main.java", StandardCharsets.UTF_8);
//        String code = ResourceUtil.readStr("testCode/unsafeCode/sleep/Main.java", StandardCharsets.UTF_8);
//        String code = ResourceUtil.readStr("testCode/unsafeCode/Memory/Main.java", StandardCharsets.UTF_8);
//        String code = ResourceUtil.readStr("testCode/unsafeCode/readFile/Main.java", StandardCharsets.UTF_8);
//        String code = ResourceUtil.readStr("testCode/unsafeCode/writeFile/Main.java", StandardCharsets.UTF_8);
        String code = ResourceUtil.readStr("testCode/unsafeCode/runFile/Main.java", StandardCharsets.UTF_8);
        request.setCode(code);
        request.setLanguage("Java");

        javaNativeCodeSandBox.executeCode(request);

    }
}
