//package com.userservice.messaging;
//
//
//import org.apache.log4j.Logger;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.EnableRabbit;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//@EnableRabbit
//@Component
//public class RabbitMqListener {
//    Logger logger = Logger.getLogger(RabbitMqListener.class);
//
//    @RabbitListener(queues = "user-queue")
//    public void processQueue2(Message message) {
//
//        logger.info("Received from queue 2: " + new String(message.getBody()));
//    }
//
//}