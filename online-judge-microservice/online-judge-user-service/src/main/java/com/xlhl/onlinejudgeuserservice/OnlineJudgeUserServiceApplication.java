package com.xlhl.onlinejudgeuserservice;

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
@MapperScan("com.xlhl.onlinejudgeuserservice.mapper")
@ComponentScan("com.xlhl")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.xlhl.onlinejudgeserviceclient.service"})
public class OnlineJudgeUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineJudgeUserServiceApplication.class, args);
    }

}
