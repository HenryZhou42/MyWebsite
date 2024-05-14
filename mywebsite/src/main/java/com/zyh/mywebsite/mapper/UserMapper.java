package com.zyh.mywebsite.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.mywebsite.entity.User;

import java.util.List;


public interface UserMapper extends BaseMapper<User> {
    List<User> getUserByUsername(String username);

}
