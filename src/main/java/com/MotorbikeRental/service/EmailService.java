package com.MotorbikeRental.service;

import com.MotorbikeRental.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.http.HttpRequest;

public interface EmailService {
    String sendVerificationEmail(User user, String url);

    String sendMail(MultipartFile[] file, String to, String[] cc, String subject, String body);

    String getSiteUrl(HttpServletRequest request);

    String sendForgotPasswordEmail(User user,String url);

    String sendChangeEmail(User user, String url,String newEmail);

}
