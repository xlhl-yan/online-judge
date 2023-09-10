package com.xlhl.onlinejudgejudgeservice.mqinit;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.xlhl.onlinejudgecommon.constant.CommonConstant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * InitRabbitMQ
 *
 * @author xlhl
 * @version 1.0
 * @description RabbitMQ交换机队列初始化
 */
@Slf4j
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Data
public class InitRabbitMq {

    private String host;

    private String username;

    private String password;

    private Integer port;

    /**
     * 项目启动时执行消息队列的初始化
     */
    @PostConstruct
    private void initRabbitMq() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            factory.setUsername(username);
            factory.setPassword(password);
            factory.setPort(port);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(CommonConstant.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            //  创建队列
            channel.queueDeclare(CommonConstant.QUEUE_NAME, true, false, false, null);
            channel.queueBind(CommonConstant.QUEUE_NAME, CommonConstant.EXCHANGE_NAME, CommonConstant.ROUTING_KEY);

            log.info("RabbitMQ 初始化成功！success");
        } catch (IOException | TimeoutException e) {
            log.error("RabbitMQ 初始化失败", e);
            e.printStackTrace();
        }
    }
}
