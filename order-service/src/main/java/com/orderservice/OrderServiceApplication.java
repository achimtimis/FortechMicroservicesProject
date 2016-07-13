package com.orderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.netflix.discovery.converters.Auto;
import com.orderservice.repository.OrderRepository;
import com.orderservice.service.OrderService;
import com.shopcommon.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableEurekaClient
@EntityScan(basePackages = "com.shopcommon.model")
public class OrderServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(OrderServiceApplication.class, args);

	}
}
