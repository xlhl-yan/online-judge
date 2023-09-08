package com.xlhl.onlinejudgecodesandbox.codesandbox.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.LogContainerResultCallback;

import java.util.List;

/**
 * DockerDemo
 *
 * @author xlhl
 * @version 1.0
 * @description Java操作Docker的一个Demo
 */
public class DockerDemo {
    public static void main(String[] args) throws InterruptedException {
        //  获取默认的   DockerClient
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        String image = "nginx:latest";
//        PullImageCmd pullImageCmd = dockerClient.pullImageCmd(image);
//        ResultCallback.Adapter<PullResponseItem> pullResponseItemAdapter = pullImageCmd.exec(new PullImageResultCallback() {
//            @Override
//            public void onNext(PullResponseItem item) {
//                System.out.println("下载镜像状态" + item.getStatus());
//                super.onNext(item);
//            }
//        }).awaitCompletion();
//        System.out.println("下载完成");

        //  region 创建容器
        CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(image);
        CreateContainerResponse response = createContainerCmd.exec();
        String responseId = response.getId();
        //  endregion

        //  region 查看容器状态
        ListContainersCmd listContainersCmd = dockerClient.listContainersCmd();
        List<Container> list = listContainersCmd.withShowAll(true).exec();
        for (Container container : list) {
            System.out.println(container);
        }
        //  endregion

        //  region 启动容器
        dockerClient.startContainerCmd(responseId).exec();
        //  endregion

        //  region 查看日志
        //  阻塞等待日志输出
        dockerClient.logContainerCmd(responseId)
                .withStdErr(true)
                .withStdOut(true)
                .exec(new LogContainerResultCallback() {
                    @Override
                    public void onNext(Frame item) {
                        System.out.println("日志：" + new String(item.getPayload()));
                        super.onNext(item);
                    }
                }).awaitCompletion();
        //endregion

        // region 删除容器
        dockerClient.removeContainerCmd(responseId).exec();
        //  endregion

        // region 删除镜像
        dockerClient.removeImageCmd(responseId).exec();
        //  endregion
    }
}
