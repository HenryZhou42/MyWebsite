package com.zyh.mywebsite.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.zyh.mywebsite.mapper")
public class MyBatisPlusConfig {
}
