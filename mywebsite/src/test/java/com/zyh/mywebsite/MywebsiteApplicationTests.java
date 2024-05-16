package com.zyh.mywebsite;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zyh.mywebsite.entity.User;
import com.zyh.mywebsite.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;


@SpringBootTest
class MywebsiteApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void contextLoads() {
	}

	@Test
	void getUserByUserName(){
		User user = new User();
		user = userMapper.getUserByUsername("testuser");
		System.out.println(user);
	}

	@Test
	void isUsernameAvailable() {
		Integer exist = userMapper.isUsernameAvailable("testuser3");
		if (exist == null || exist == 0) {
			System.out.println("Is available"); // 如果查询结果为0或null，表示用户名可用
		} else {
			System.out.println("Is not available"); // 否则，表示用户名已被占用
		}
	}

	@Test
	void testRedis(){
		redisTemplate.opsForValue().set("username","testuserx");
		String username = redisTemplate.opsForValue().get("username");
		System.out.println(username);
	}

	@Test
	void testRedis2() throws IOException {
		User user = new User();
		user.setUsername("testuserx");
		user.setPassword("123456");
		String json = objectMapper.writeValueAsString(user);
		redisTemplate.opsForValue().set("user",json);
		String userJson = redisTemplate.opsForValue().get("user");

		User user1 = objectMapper.readValue(userJson, User.class);
		System.out.println(user1);
	}

}
