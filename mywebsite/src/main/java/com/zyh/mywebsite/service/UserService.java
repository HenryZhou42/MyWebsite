package com.zyh.mywebsite.service;

import com.zyh.mywebsite.entity.User;
import com.zyh.mywebsite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    public User register(User user) {
        if (!isUsernameAvailable(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        return userRepository.save(user);
    }

    public User authenticateUser(User user) {
        // 根据用户名查找用户
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());

        // 检查用户是否存在且密码匹配
        if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(user.getPassword())) {
            return optionalUser.get(); // 认证成功，返回用户对象
        }

        return null;  // 用户名不存在或密码不匹配，返回null
    }

}
