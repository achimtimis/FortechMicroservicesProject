package com.orderservice.messaging;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@EnableRabbit
@Component
public class RabbitMqListener {
    Logger logger = Logger.getLogger(RabbitMqListener.class);

    @RabbitListener(queues = "queue2")
    public void processQueue1(String message) {
        logger.info("Received from queue 2: " + message);
    }

}