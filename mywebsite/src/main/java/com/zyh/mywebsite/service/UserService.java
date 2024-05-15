package com.zyh.mywebsite.service;

import com.zyh.mywebsite.entity.User;
import com.zyh.mywebsite.mapper.UserMapper;
import com.zyh.mywebsite.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public boolean isUsernameAvailable(String username) {
        Integer exist = userMapper.isUsernameAvailable(username);
        if (exist == null || exist == 0) {
            return true;// 如果查询结果为0或null，表示用户名可用
        } else {
            return false; // 否则，表示用户名已被占用
        }
    }
    public User register(User user) {
        if (isUsernameAvailable(user.getUsername())) {
            // 使用MyBatisPlus的insert方法
            int rowsAffected = userMapper.insertNewUser(user);
            if (rowsAffected > 0) {
                // 插入成功后，如果需要可以在此处根据刚插入的用户名称查询完整的用户信息
                return user;
            } else {
                // 处理插入失败的情况，比如抛出异常或返回错误信息
                throw new RuntimeException("Failed to insert user");
            }
        }else {
            throw new IllegalArgumentException("Username already exists");
        }
}
    public User authenticateUser(User user) {
        // 根据用户名查找用户
        Optional<User> optionalUser = Optional.ofNullable(userMapper.getUserByUsername(user.getUsername()));

        // 检查用户是否存在且密码匹配
        if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(user.getPassword())) {
            return optionalUser.get(); // 认证成功，返回用户对象
        }
        return null;  // 用户名不存在或密码不匹配，返回null
    }

}
