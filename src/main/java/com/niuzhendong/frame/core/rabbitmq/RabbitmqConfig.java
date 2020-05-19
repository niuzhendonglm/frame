package com.niuzhendong.frame.core.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ的消息确认配置
 * @author niuzhendong
 */
@Configuration
@Slf4j
public class RabbitmqConfig {
    @Autowired
    private ConnectionFactory connectionFactory;

    /**
     * 重写 RabbitTemplate
     * @return RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        /**
         * 消息确认，消息发送到交换机执行的方法
         */
        rabbitTemplate.setConfirmCallback(((correlationData, ack, cause) -> {
            if(ack) {
                log.info("消息发送到exchange成功，id:{}",correlationData.getId());
            }else{
                log.error("消息发送到exchange失败，原因：{}",cause);
            }
        }));

        /**
         * 消息返回，消息发送到队列，出现错误时执行的方法
         */
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingkey) -> {
            String correlationId = message.getMessageProperties().getCorrelationId();
            log.error("消息：{}，应答码：{}，原因：{}，交换机：{}，路由键：{}",correlationId, replyCode, replyText, exchange, routingkey);
        });

        return rabbitTemplate;
    }
}