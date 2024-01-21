package com.example.dish;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Slf4j
@SpringBootApplication
public class DishApplication {
	public static void main(String[] args) {
		SpringApplication.run(DishApplication.class, args);
		log.info("项目启动成功");
	}

}
