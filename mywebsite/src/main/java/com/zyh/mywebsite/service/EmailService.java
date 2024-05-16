package com.zyh.mywebsite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
@Autowired
private JavaMailSender javaMailSender;

    public void sendWelcomeEmail(String recipientEmail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();

        // 设置收件人，测试阶段，收件人设为默认邮箱
        message.setTo(recipientEmail);
        // 设置主题
        message.setSubject("Welcome to Our Website, " + username + "!");
        // 设置邮件正文
        message.setText("Dear " + username + ",\n\nWelcome to our website! We're excited to have you on board.\n\nBest regards,\nThe Team");

        // 测试阶段，使用我的默认邮箱发送
        message.setFrom("zyhhenry42@hotmail.com");

        try {
            javaMailSender.send(message);
        } catch (Exception ex) {
            // 记录下发送失败的日志
            System.err.println("Failed to send email: " + ex.getMessage());
        }
    }
}