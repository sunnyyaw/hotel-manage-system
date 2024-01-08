package com.example.dish;

import com.example.dish.config.StorageProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.example.dish.mapper")
@EnableConfigurationProperties(StorageProperties.class)
@EnableTransactionManagement
@SpringBootApplication
public class DishApplication {

	public static void main(String[] args) {
		SpringApplication.run(DishApplication.class, args);
	}

}
