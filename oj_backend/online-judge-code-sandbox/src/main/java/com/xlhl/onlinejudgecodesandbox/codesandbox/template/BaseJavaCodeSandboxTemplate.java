package com.xlhl.onlinejudgecodesandbox.codesandbox.template;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.xlhl.onlinejudgecodesandbox.codesandbox.model.ExecuteCodeRequest;
import com.xlhl.onlinejudgecodesandbox.codesandbox.model.ExecuteCodeResponse;
import com.xlhl.onlinejudgecodesandbox.codesandbox.model.ExecuteMessage;
import com.xlhl.onlinejudgecodesandbox.codesandbox.model.JudgeInfo;
import com.xlhl.onlinejudgecodesandbox.codesandbox.service.CodeSandBox;
import com.xlhl.onlinejudgecodesandbox.codesandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * BaseJavaCodeSandboxTemplate
 *
 * @author xlhl
 * @version 1.0
 * @description
 */
@Slf4j
public abstract class BaseJavaCodeSandboxTemplate implements CodeSandBox {

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
     * 调用代码沙箱执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        log.info("传入的代码为:{}", code);

        //  region 1.把代码写入到临时文件中
        File userCodeFile = saveCodeFile(code);
        //  endregion

        //  region 2.编译代码得到class文件
        ExecuteMessage compileFileExecuteMessage = compileFile(userCodeFile);
        System.out.println(compileFileExecuteMessage);
        //  endregion

        //  region 3.执行代码得到输出结果
        List<ExecuteMessage> outputList = runFile(userCodeFile, inputList);
        System.out.println(outputList);
        //  endregion

        //  region 4. 收集整理输出结果
        ExecuteCodeResponse outputResponse = getOutputResponse(outputList);
        //  endregion

        //  region  5.文件清理
        deleteFile(userCodeFile);
        //  endregion
        return outputResponse;
    }

    /**
     * 1.把用户代码保存为临时文件
     *
     * @param code 用户代码
     * @return 临时文件
     */
    public File saveCodeFile(String code) {
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
        return FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
    }

    /**
     * 2.编译代码
     *
     * @param userCodeFile
     * @return
     */
    public ExecuteMessage compileFile(File userCodeFile) {
        String compileCmd = String.format("javac -encoding UTF-8 %s", userCodeFile.getAbsolutePath());
        log.info("执行命令：{}", compileCmd);
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            //  等待程序执行获取状态码
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            if (executeMessage.getExitValue() != 0) {
                throw new RuntimeException("执行结果出现错误");
            }
            return executeMessage;
        } catch (Exception e) {
            //  return getErrorResponse(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 3.执行文件，获得执行结果列表
     *
     * @param userCodeFile
     * @param inputList
     * @return
     */
    public List<ExecuteMessage> runFile(File userCodeFile, List<String> inputList) {
        List<ExecuteMessage> outputList = new ArrayList<>();
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        for (String input : inputList) {
            String runCmd = String.format(
                    "java -Xmx256m -Dfile.encoding=UTF-8 -cp %s  Main %s",
                    userCodeParentPath, input);

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
                        log.error("出现异常：", e);
                        e.printStackTrace();
                    }
                });
                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
                outputList.add(executeMessage);
            } catch (Exception e) {
                throw new RuntimeException("程序执行异常");
            }
        }
        return outputList;
    }

    /**
     * 4.整体输出结果
     *
     * @param outputList
     * @return
     */
    public ExecuteCodeResponse getOutputResponse(List<ExecuteMessage> outputList) {
        ExecuteCodeResponse response = new ExecuteCodeResponse();
        List<String> outputMessageList = new ArrayList<>();
        long maxTime = 0L;
        long maxMemory = 0L;
        for (ExecuteMessage executeMessage : outputList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                response.setMessage(errorMessage);
                //  用户提交的代码执行出现错误 417
                response.setStatus(HttpStatus.EXPECTATION_FAILED.value());
                break;
            }
            if (StrUtil.isNotBlank(executeMessage.getMessage())) {
                outputMessageList.add(executeMessage.getMessage());
            }
            //  取最大值判断是否超时
            Long timeConsuming = executeMessage.getTimeConsuming();
            if (timeConsuming != null) {
                maxTime = Math.max(maxTime, timeConsuming);
            }
            //  取最大的内存消耗
            Long memoryConsuming = executeMessage.getMemoryConsuming();
            if (memoryConsuming != null) {
                maxMemory = Math.max(maxMemory, memoryConsuming);
            }
        }
        if (outputMessageList.size() == outputList.size()) {
            //  正常运行完成  200
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(outputMessageList.toString());
        }
        response.setOutputList(outputMessageList);


        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMemory(maxMemory);
        judgeInfo.setMessage(outputMessageList.toString());
        judgeInfo.setTime(maxTime);

        response.setJudgeInfo(judgeInfo);

        return response;
    }

    /**
     * 5.删除临时文件
     *
     * @param userCodeFile
     * @return
     */
    public Boolean deleteFile(File userCodeFile) {
        boolean del = true;
        if (userCodeFile.getParent() != null) {
            String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
            del = FileUtil.del(userCodeParentPath);
            if (del) {
                log.info("文件删除成功:{}", userCodeParentPath);
            } else {
                log.error("文件删除失败:{}", userCodeParentPath);
            }
        }
        return del;
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

}

