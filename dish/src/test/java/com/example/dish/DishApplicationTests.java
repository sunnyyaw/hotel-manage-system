package com.example.dish;

import com.example.dish.entity.Dish;
import com.example.dish.mapper.DishMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@SpringBootTest
class DishApplicationTests {
	@Autowired
	DataSource dataSource;

	@Test
	void contextLoads() throws SQLException {
		System.out.println(dataSource.getClass());
		Connection connection = dataSource.getConnection();
		System.out.println(connection);
		connection.close();
	}
	@Autowired
	DishMapper dishMapper;
	@Test
	public void toTest(){
		List<Dish> dishes = dishMapper.getAllDishes();
		dishes.forEach(System.out::println);
	}
}
