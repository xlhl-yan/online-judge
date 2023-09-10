package com.xlhl.onlinejudgejudgeservice.rabbitmq;

import com.rabbitmq.client.Channel;
import com.xlhl.onlinejudgecommon.constant.CommonConstant;
import com.xlhl.onlinejudgejudgeservice.judge.JudgeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * MessageConsumer
 *
 * @author : xlhl
 * @date : 2023/9/10 14:32
 */
@Component
@Slf4j
public class MessageConsumer implements ChannelAwareMessageListener {
    @Resource
    private JudgeService judgeService;

    @Override
    @RabbitListener(queues = CommonConstant.QUEUE_NAME, ackMode = "MANUAL")
    public void onMessage(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String result = new String(message.getBody());
            Long questionSubmitId = Long.valueOf(result);
            judgeService.doJudge(questionSubmitId);
            log.info("message:{}", result);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("消息处理失败：", e);
            e.printStackTrace();
            channel.basicNack(deliveryTag, false, false);
        }
    }
}