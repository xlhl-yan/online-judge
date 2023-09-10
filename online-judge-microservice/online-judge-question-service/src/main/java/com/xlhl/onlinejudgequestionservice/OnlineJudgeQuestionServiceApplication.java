package com.xlhl.onlinejudgequestionservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Yz150
 */
@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@MapperScan("com.xlhl.onlinejudgequestionservice.mapper")
@ComponentScan("com.xlhl")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.xlhl.onlinejudgeserviceclient.service"})
public class OnlineJudgeQuestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineJudgeQuestionServiceApplication.class, args);
    }

}