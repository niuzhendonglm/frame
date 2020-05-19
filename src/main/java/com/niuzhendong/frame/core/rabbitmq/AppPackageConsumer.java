package com.niuzhendong.frame.core.rabbitmq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class AppPackageConsumer {
    /**
     * 监听队列appPackageQueue
     * @param message
     * @param channel
     */
    @RabbitListener(queues = "appPackageQueue")
    public void receive(Message message, Channel channel) {
        try {
            //手动确认，消息已经成功消费，不批量接收
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("Receive Success from Queue：{}", new String(message.getBody()));
        } catch (IOException e) {
            e.printStackTrace();
            try {
                //手动确认，消息消费失败，不批量拒绝，重入队列
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                log.error("Receive Fail from Queue：{}", new String(message.getBody()));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}