package com.zyh.mywebsite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.mywebsite.entity.User;

public interface UserMapper extends BaseMapper<User> {
    User getUserByUsername(String username);

    Integer isUsernameAvailable(String username);

    Integer insertNewUser(User user);
}
