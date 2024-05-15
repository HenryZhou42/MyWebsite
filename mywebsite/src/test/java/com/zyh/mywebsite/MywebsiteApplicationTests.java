package com.zyh.mywebsite;

import com.zyh.mywebsite.entity.User;
import com.zyh.mywebsite.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MywebsiteApplicationTests {

    @Autowired
    private UserMapper userMapper;

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
	void insertUser(){
		User user = new User();
		user.setUsername("testuser89");
		user.setPassword("123456");
		userMapper.insert(user);
		System.out.println(user);
	}

}
