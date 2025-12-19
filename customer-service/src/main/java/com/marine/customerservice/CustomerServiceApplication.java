package com.marine.customerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class CustomerServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}
}
