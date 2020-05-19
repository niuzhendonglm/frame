//package com.niuzhendong.frame.core.rabbitmq;
//
//import org.springframework.amqp.rabbit.connection.CorrelationData;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.UUID;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@Component
//public class AppPackageConfigTest {
//    @Autowired
//    RabbitTemplate rabbitTemplate;
//
//    @Test
//    public void testAppPackage() {
//        // 生成消息校验
//        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
//        rabbitTemplate.convertAndSend("appPackageDirectExchange","appPackageDirect","appPackage",correlationData);
//    }
//}