package com.marine.supplierservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SupplierServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupplierServiceApplication.class, args);
	}

}
