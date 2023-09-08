package com.xlhl.onlinejudgecodesandbox.codesandbox.utils;

import cn.hutool.core.util.StrUtil;
import com.xlhl.onlinejudgecodesandbox.codesandbox.model.ExecuteMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StopWatch;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ProcessUtils
 *
 * @author xlhl
 * @version 1.0
 * @description 进程工具类
 */
public class ProcessUtils {

    /**
     * 执行并获取程序执行信息
     *
     * @param runProcess
     * @param opName
     * @return
     */
    public static ExecuteMessage runProcessAndGetMessage(Process runProcess, String opName) {
        ExecuteMessage executeMessage = new ExecuteMessage();
        StopWatch stopWatch = new StopWatch();
        try {
            //  开启计时
            stopWatch.start();
            int exitValue = runProcess.waitFor();
            executeMessage.setExitValue(exitValue);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
            if (exitValue == 0) {
                //  正常退出
                System.out.println(opName + "成功");
                //  分批获取进程的正常输出流

                String compileOutputLine;
                List<String> outputList = new ArrayList<>();
                //  循环读取
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    outputList.add(compileOutputLine);
                }
                String output = StringUtils.join(outputList, "\n");
                executeMessage.setMessage(output);
            } else {
                //  异常退出
                System.out.println(opName + "失败，错误码：" + exitValue);
                //  分批获取进程的正常输出流
                String compileOutputLine;
                List<String> outputList = new ArrayList<>();
                //  循环读取
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    outputList.add(compileOutputLine);
                }
                System.out.println(outputList);
                String output = StringUtils.join(outputList, "\n");
                executeMessage.setMessage(output);

                //  分批获取进程的错误输出流
                BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
                String errorCompileOutputLine;
                List<String> errorOutputList = new ArrayList<>();
                //  循环读取
                while ((errorCompileOutputLine = errorBufferedReader.readLine()) != null) {
                    errorOutputList.add(errorCompileOutputLine);
                }

                String errorOutput = StringUtils.join(errorOutputList, "\n");
                executeMessage.setErrorMessage(errorOutput);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        //  停止计时
        stopWatch.stop();
        //  获取消耗时间
        long timeMillis = stopWatch.getLastTaskTimeMillis();
        executeMessage.setTimeConsuming(timeMillis);
        return executeMessage;
    }

    /**
     * 执行交互式程序并获取程序执行信息
     *
     * @param runProcess
     * @return
     */
    public static ExecuteMessage runInterProcessAndGetMessage(Process runProcess, String args) {
        ExecuteMessage executeMessage = new ExecuteMessage();
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {

            //  向控制台输入程序
            outputStream = runProcess.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream);
            String[] split = args.split(" ");
            String join = StrUtil.join("\n", split) + "\n";
            outputStreamWriter.write(join);
            //  相当于执行输入发送（回车 / enter）
            outputStreamWriter.flush();

            //  正常退出
            //  分批获取进程的正常输出流
            inputStream = runProcess.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String compileOutputLine;
            StringBuilder compileOutputBuilder = new StringBuilder();
            //  循环读取
            while ((compileOutputLine = bufferedReader.readLine()) != null) {
                compileOutputBuilder.append(compileOutputLine);
            }
            System.out.println(compileOutputBuilder);
            executeMessage.setMessage(compileOutputBuilder.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //  region 释放资源
            //  一定要记得资源的释放，否则会卡死
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (runProcess != null) {
                runProcess.destroy();
            }
            //  endregion
        }
        return executeMessage;
    }
}
