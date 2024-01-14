package com.example.dish;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.example.dish.mapper")
@EnableTransactionManagement
@SpringBootApplication
public class DishApplication {
	public static void main(String[] args) {
		SpringApplication.run(DishApplication.class, args);
	}

}
