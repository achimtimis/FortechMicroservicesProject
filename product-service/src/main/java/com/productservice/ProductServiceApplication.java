package com.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EntityScan(basePackages = "com.shopcommon.model")
public class ProductServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(ProductServiceApplication.class, args);
	}

}
