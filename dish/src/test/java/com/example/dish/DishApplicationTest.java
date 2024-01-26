package com.example.dish;

import com.example.dish.common.Query;
import com.example.dish.entity.Comment;
import com.example.dish.mapper.CommentMapper;
import com.example.dish.mapper.DishMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class DishApplicationTest {
	@Autowired
	private DataSource dataSource;
	private HttpSession session;
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void contextLoads() throws SQLException {
		System.out.println(dataSource.getClass());
		Connection connection = dataSource.getConnection();
		System.out.println(connection);
		connection.close();
	}
	@Before
	@Test
	public void Login() throws Exception {
		String json = "{\"username\":\"sunny\"," +
				"\"password\":\"123\"," +
				"\"verifyCode\":\"aefnmss\"}";
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.post("/login")
				.content(json)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		session = result.getRequest().getSession();
	}
	@Test
	public void getAllUsers() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.get("/users")
				.content("")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
						.session((MockHttpSession) session))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	@Test
	public void listUsers() throws Exception {
		Query query = new Query();
		query.put("currentPage",1);
		query.put("pageSize",5);
		String json = new ObjectMapper().writeValueAsString(query);
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
						.get("/users")
						.content(json)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.session((MockHttpSession) session))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		mockMvc.perform(MockMvcRequestBuilders
						.get("/users")
						.content("")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.session((MockHttpSession) session))
				.andExpect(MockMvcResultMatchers.status().isOk());
		System.out.println(result.getResponse().getContentAsString());
	}
	@Test
	public void listBills() throws Exception {
		Query query = new Query();
		query.put("currentPage",1);
		query.put("pageSize",5);
		String json = new ObjectMapper().writeValueAsString(query);
		mockMvc.perform(MockMvcRequestBuilders
						.get("/bills")
						.content(json)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.session((MockHttpSession) session))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		mockMvc.perform(MockMvcRequestBuilders
						.get("/bills")
						.content("")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.session((MockHttpSession) session))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}
}
