package com.xlhl.onlinejudgecodesandbox.codesandbox.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.xlhl.onlinejudgecodesandbox.codesandbox.model.ExecuteCodeRequest;
import com.xlhl.onlinejudgecodesandbox.codesandbox.model.ExecuteCodeResponse;
import com.xlhl.onlinejudgecodesandbox.codesandbox.model.ExecuteMessage;
import com.xlhl.onlinejudgecodesandbox.codesandbox.model.JudgeInfo;
import com.xlhl.onlinejudgecodesandbox.codesandbox.service.CodeSandBox;
import com.xlhl.onlinejudgecodesandbox.codesandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * JavaNativeCodeSandBox
 *
 * @author xlhl
 * @version 1.0
 * @description Java原生代码沙箱
 */
@Component
@Slf4j
public class JavaDockerCodeSandBox implements CodeSandBox {

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
     * 自定义 Security Manager 字节码文件所在的绝对路径
     */
    private static final String SECURITY_PATH = "C:\\JavaCode\\online-oj\\oj_backend\\online-judge-code-sandbox\\src\\main\\resources\\security";

    /**
     * 自定义 Security Manager 字节码文件名称
     */
    private static final String SECURITY_CLASS_NAME = "OnlineJudgeSecurityManager";

    private static final Boolean FIRST_INIT = true;

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
        String language = executeCodeRequest.getLanguage();
        System.out.println("传入代码为：{\n" + code + "}");

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
        //  创建容器，把文件复制到容器内
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();

        ExecuteMessage executeMessage = new ExecuteMessage();
        final String[] message = {null};
        final String[] errorMessage = {null};

        String image = "openjdk:8-alpine";
        if (FIRST_INIT) {
            PullImageCmd pullImageCmd = dockerClient.pullImageCmd(image);
            try {
                ResultCallback.Adapter<PullResponseItem> pullResponseItemAdapter = pullImageCmd.exec(new PullImageResultCallback() {
                    @Override
                    public void onNext(PullResponseItem item) {
                        System.out.println("下载镜像状态" + item.getStatus());
                        super.onNext(item);
                    }
                }).awaitCompletion();
            } catch (InterruptedException e) {
                System.err.println("拉取镜像异常" + e.getMessage());
                e.printStackTrace();
            }
        }


        CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(image);
        //  创建容器时指定文件路径（Volume）映射，=> 将本地文件同步到容器中
        HostConfig hostConfig = new HostConfig();
        hostConfig.setBinds(new Bind(userCodeParentPath, new Volume("/judge")));
        //  限制内存
        hostConfig.withMemory(1024 * 1024 * 100L);
        hostConfig.withMemorySwap(0L);
        hostConfig.withCpuCount(1L);
        hostConfig.withSecurityOpts(Collections.singletonList("seccomp = {  \n" +
                "    \"localhost/profile.json\": {  \n" +
                "        \"type\": \"Seccomp\",  \n" +
                "        \"data\": {  \n" +
                "            \"arch\": \"amd64\",  \n" +
                "            \"mode\": \"strict\",  \n" +
                "            \"syscalls\": [  \n" +
                "                {  \n" +
                "                    \"names\": [\"chmod\", \"fchmod\", \"fchmodat\"],  \n" +
                "                    \"action\": \"SCMP_ACT_ERRNO\"  \n" +
                "                },  \n" +
                "                {  \n" +
                "                    \"names\": [\"getcwd\", \"getwd\"],  \n" +
                "                    \"action\": \"SCMP_ACT_ERRNO\"  \n" +
                "                },  \n" +
                "                {  \n" +
                "                    \"names\": [\"chown\", \"fchown\", \"fchownat\", \"lchown\"],  \n" +
                "                    \"action\": \"SCMP_ACT_ERRNO\"  \n" +
                "                },  \n" +
                "                {  \n" +
                "                    \"names\": [\"readlink\", \"readlinkat\"],  \n" +
                "                    \"action\": \"SCMP_ACT_ERRNO\"  \n" +
                "                },  \n" +
                "                {  \n" +
                "                    \"names\": [\"set_thread_area\", \"arch_prctl\"],  \n" +
                "                    \"action\": \"SCMP_ACT_ERRNO\"  \n" +
                "                },  \n" +
                "                {  \n" +
                "                    \"names\": [\"sysinfo\", \"times\"],  \n" +
                "                    \"action\": \"SCMP_ACT_ERRNO\"  \n" +
                "                },  \n" +
                "                {  \n" +
                "                    \"names\": [\"getrandom\", \"getentropy\", \"urandom_read\"],  \n" +
                "                    \"action\": \"SCMP_ACT_ERRNO\"  \n" +
                "                },  \n" +
                "                {  \n" +
                "                    \"names\": [\"set_robust_list\", \"get_robust_list\"],  \n" +
                "                    \"action\": \"SCMP_ACT_ERRNO\"  \n" +
                "                },  \n" +
                "                {  \n" +
                "                    \"names\": [\"epoll_create1\", \"epoll_ctl\", \"epoll_wait\"],  \n" +
                "                    \"action\": \"SCMP_ACT_ERRNO\"  \n" +
                "                },  \n" +
                "                {  \n" +
                "                    \"names\": [\"mlock2\", \"munlockall\", \"munmap\"],  \n" +
                "                    \"action\": \"SCMP_ACT_ERRNO\"  \n" +
                "                },  \n" +
                "                {  \n" +
                "                    \"names\": [\"set_tid_address\", \"gettid\"],  \n" +
                "                    \"action\": \"SCMP_ACT_ERRNO\"  \n" +
                "                },  \n" +
                "                {  \n" +
                "                    \"names\": [\"set_robust_list\", \"get_robust_list\"],  \n" +
                "                    \"action\": \"SCMP_ACT_ERRNO\"  \n" +
                "                },  \n" +
                "                {  \n" +
                "                    \"names\": [\"io_uring_setup\", \"io_uring_enter\", \"io_uring_submit\"],  \n" +
                "                    \"action\": \"SCMP_ACT_ERRNO\"  \n" +
                "                }  \n" +
                "            ]  \n" +
                "        }  \n" +
                "    }  \n" +
                "}"));
        CreateContainerResponse containerResponse = createContainerCmd
                .withReadonlyRootfs(true)
                .withNetworkDisabled(true)
                .withHostConfig(hostConfig)
                .withAttachStderr(true)
                .withAttachStdin(true)
                .withAttachStdout(true)
                .withTty(true)
                .exec();

        String containerId = containerResponse.getId();
        dockerClient.startContainerCmd(containerId).exec();


        ArrayList<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList) {
            StopWatch stopWatch = new StopWatch();
            String[] args = inputArgs.split(" ");
            //               镜像名称 ↓
            //  docker exec  kind_benz java -cp /judge Main 1 3
            String[] cmdArray = ArrayUtil.append(new String[]{"java", "-cp", "/judge", "Main"}, args);

            ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                    .withCmd(cmdArray)
                    .withAttachStdin(true)
                    .withAttachStdout(true)
                    .withAttachStderr(true)
                    .exec();

            System.out.println("执行创建命令" + execCreateCmdResponse);

            String execId = execCreateCmdResponse.getId();
            if (StrUtil.isBlank(execId)) {
                System.err.println("执行id为空~~");
            }

            AtomicReference<Long> reference = new AtomicReference<>(0L);
            //  获取占用的内存
            StatsCmd statsCmd = dockerClient.statsCmd(containerId);
            statsCmd.exec(new ResultCallback<Statistics>() {
                @Override
                public void onNext(Statistics object) {
                    Long memory = object.getMemoryStats().getUsage();
                    if (memory != null) {
                        System.out.println("内存占用为：" + memory);
                        reference.set(Math.max(reference.get(), memory));
                    }
                }

                @Override
                public void onStart(Closeable closeable) {

                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {

                }

                @Override
                public void close() throws IOException {

                }
            });

            final boolean[] exceedTime = {true};
            ExecStartResultCallback execStartResultCallback = new ExecStartResultCallback() {
                @Override
                public void onNext(Frame frame) {
                    StreamType streamType = frame.getStreamType();
                    if (streamType == StreamType.STDERR) {
                        errorMessage[0] = new String(frame.getPayload());
                        System.err.println("输出错误结果" + errorMessage[0]);
                    } else {
                        message[0] = new String(frame.getPayload());
                        System.out.println("输出结果" + message[0]);
                    }
                    super.onNext(frame);
                }

                @Override
                public void onComplete() {
                    //  如果设置完成，代表着未超时
                    exceedTime[0] = false;
                    super.onComplete();
                }
            };
            try {
                stopWatch.start();
                dockerClient.execStartCmd(execId)
                        .exec(execStartResultCallback)
                        .awaitCompletion(TIMEOUT, TimeUnit.MILLISECONDS);
                stopWatch.stop();
                statsCmd.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            executeMessage.setMessage(message[0]);
            executeMessage.setErrorMessage(errorMessage[0]);
            executeMessage.setMemoryConsuming(reference.get());
            executeMessage.setTimeConsuming(stopWatch.getLastTaskTimeMillis());
            executeMessageList.add(executeMessage);
        }


        return executeCodeResponse;
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
        JavaDockerCodeSandBox javaNativeCodeSandBox = new JavaDockerCodeSandBox();
        ExecuteCodeRequest request = new ExecuteCodeRequest();
        request.setInputList(Arrays.asList("1 2", "1 3"));

        String code = ResourceUtil.readStr("testCode/simpleComputeArgs/Main.java", StandardCharsets.UTF_8);
//        String code = ResourceUtil.readStr("testCode/unsafeCode/sleep/Main.java", StandardCharsets.UTF_8);
//        String code = ResourceUtil.readStr("testCode/unsafeCode/Memory/Main.java", StandardCharsets.UTF_8);
//        String code = ResourceUtil.readStr("testCode/unsafeCode/readFile/Main.java", StandardCharsets.UTF_8);
//        String code = ResourceUtil.readStr("testCode/unsafeCode/writeFile/Main.java", StandardCharsets.UTF_8);
//        String code = ResourceUtil.readStr("testCode/unsafeCode/runFile/Main.java", StandardCharsets.UTF_8);
        request.setCode(code);
        request.setLanguage("Java");

        javaNativeCodeSandBox.executeCode(request);

    }
}
