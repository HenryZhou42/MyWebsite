package com.zyh.mywebsite.service;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zyh.mywebsite.entity.User;
import com.zyh.mywebsite.entity.UserRegistrationEvent;
import com.zyh.mywebsite.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    private final static String CACHE_KEY_USERNAME_AVAILABLE = "user:availability:";
    private final static String CACHE_KEY_USER_INFO = "user:info:";
    private final static String CACHE_KEY_USER_AUTH = "user:auth:";

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public boolean isUsernameAvailable(String username) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        Boolean isAvailable = ops.get(CACHE_KEY_USERNAME_AVAILABLE + username) != null ?
                (Boolean) ops.get(CACHE_KEY_USERNAME_AVAILABLE + username) : null;

        if (isAvailable == null) {
            Integer exist = userMapper.isUsernameAvailable(username);
            isAvailable = (exist == null || exist == 0);
            ops.set(CACHE_KEY_USERNAME_AVAILABLE + username, isAvailable, 10, TimeUnit.MINUTES); // 设置缓存5分钟过期
        }
        return isAvailable;
    }

    public User register(User user) {
        if (isUsernameAvailable(user.getUsername())) {
            int rowsAffected = userMapper.insertNewUser(user);
            if (rowsAffected > 0) {
                UserRegistrationEvent event = new UserRegistrationEvent(user.getUsername());
                jmsTemplate.convertAndSend("user.registration.queue", event);
                // 注册成功后，将用户信息存入Redis
                redisTemplate.opsForValue().set(CACHE_KEY_USER_INFO + user.getUsername(), user, 10, TimeUnit.MINUTES);
                return user;
            } else {
                throw new RuntimeException("Failed to insert user into the database.");
            }
        } else {
            throw new IllegalArgumentException("The username already exists.");
        }

    }

    @JmsListener(destination = "user.registration.queue")
    public void handleUserRegistration(UserRegistrationEvent event) {

        try {
            String username = event.getUsername();
            String testRecipientEmail = "1290420016@qq.com";
            emailService.sendWelcomeEmail(testRecipientEmail, username);
            if (StringUtils.isEmpty(username)) {
                logger.warn("Received UserRegistrationEvent with empty username, skipping sending email.");
            }
        } catch (Exception e) {
            //记录详细的错误信息到日志
            logger.error("Failed to send welcome email for user registration due to: ", e);
        }
    }

    public User authenticateUser(User user) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        // 尝试从Redis中获取缓存的用户认证信息
        String cacheKey = CACHE_KEY_USER_AUTH + user.getUsername();
        User cachedUser = (User) ops.get(cacheKey);
        if (cachedUser != null) {
            // 如果缓存中存在用户，直接验证密码
            if (cachedUser.getPassword().equals(user.getPassword())) {
                return cachedUser; // 密码匹配，认证成功
            } else {
                return null; // 密码不匹配
            }
        }else {
            // 缓存未命中，从数据库查询
            Optional<User> optionalUser = Optional.ofNullable(userMapper.getUserByUsername(user.getUsername()));
            if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(user.getPassword())) {
                // 认证成功，将用户信息存入Redis
                ops.set(cacheKey, optionalUser.get(), 10, TimeUnit.MINUTES); //设置缓存10分钟过期
                return optionalUser.get();
            } else {
                return null; // 用户不存在或密码不匹配
            }
        }
    }
}
