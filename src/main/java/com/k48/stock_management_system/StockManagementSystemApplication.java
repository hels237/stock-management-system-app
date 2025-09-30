package com.k48.stock_management_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
@EnableJpaAuditing
@SpringBootApplication
public class StockManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockManagementSystemApplication.class, args);
	}

}
