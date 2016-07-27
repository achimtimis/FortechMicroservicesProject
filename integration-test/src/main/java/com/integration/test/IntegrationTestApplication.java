package com.integration.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.shopcommon.model")
public class IntegrationTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntegrationTestApplication.class, args);
	}
}
