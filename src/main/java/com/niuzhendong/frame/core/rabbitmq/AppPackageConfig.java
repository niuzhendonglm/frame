package com.niuzhendong.frame.core.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppPackageConfig {
    /**
     * 新建队列appPackageQueue
     * @return Queue
     */
    @Bean
    public Queue appPackageQueue() {
        return new Queue("appPackageQueue");
    }

    /**
     * 新建交换机appPackageDirectExchange
     * @return
     */
    @Bean
    public DirectExchange appPackageDirectExchange() {
        return new DirectExchange("appPackageDirectExchange");
    }

    /**
     * 将消息队列绑定到交换机
     * @return Binding
     */
    @Bean
    public Binding bindAppPackageQueue() {
        return BindingBuilder.bind(appPackageQueue()).to(appPackageDirectExchange()).with("appPackageDirect");
    }
}
