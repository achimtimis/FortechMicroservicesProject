package com.userservice;

import org.mockito.Mockito;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Created by Flaviu Cicio on 26.07.2016.
 */
public class TestMockito {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Bean
    @Primary
    private RabbitTemplate registerRabbitTemplate() {
        return Mockito.spy(rabbitTemplate);
    }

}
