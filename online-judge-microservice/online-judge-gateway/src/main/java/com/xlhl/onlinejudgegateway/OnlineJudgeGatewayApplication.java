package com.xlhl.onlinejudgegateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Yz150
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.xlhl.onlinejudgeserviceclient.service"})
public class OnlineJudgeGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineJudgeGatewayApplication.class, args);
    }

}
