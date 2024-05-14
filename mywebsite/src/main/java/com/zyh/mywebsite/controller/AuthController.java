package com.zyh.mywebsite.controller;

import com.zyh.mywebsite.entity.User;
import com.zyh.mywebsite.entity.UserNameAvailabilityResponse;
import com.zyh.mywebsite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value ={"/api/auth"})
@ResponseBody
public class AuthController {
    @Autowired
    private UserService userService;
    @PostMapping(value = {"/register"})
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        try {
            // 尝试注册用户
            User newUser = userService.register(user);
            if (newUser != null) {
                // 注册成功
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("status", "success");
                responseBody.put("message", "User registered successfully");
                responseBody.put("user", newUser);
                return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
            } else {
                // 这里假设如果newUser为null，表示注册失败（可能因为用户名已存在等情况）
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("status", "error");
                responseBody.put("message", "Registration failed, user may already exist");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
            }
        } catch (Exception e) {
            // 处理其他异常情况
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", "error");
            responseBody.put("message", "An error occurred during registration");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }
    @PostMapping(value = {"/login"})
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        try {
            User authenticatedUser = userService.authenticateUser(user);
            if (authenticatedUser != null) {
                // 登录成功
                Map<String, String> responseBody = new HashMap<>();
                responseBody.put("status", "success");
                responseBody.put("message", "Login successful");
                return ResponseEntity.ok(responseBody);
            } else {
                // 用户名或密码错误
                Map<String, String> responseBody = new HashMap<>();
                responseBody.put("status", "error");
                responseBody.put("message", "Invalid username or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
            }
        } catch (Exception e) {
            // 登录过程中发生异常
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("status", "error");
            responseBody.put("message", "An error occurred during login");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }
    @GetMapping("/check-username")
    public ResponseEntity<UserNameAvailabilityResponse> checkUsernameAvailability(@RequestParam("username") String username) {
        boolean isAvailable = userService.isUsernameAvailable(username);

        UserNameAvailabilityResponse response = new UserNameAvailabilityResponse(username, isAvailable);

        return ResponseEntity.ok(response);
    }
}