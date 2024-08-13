package com.MotorbikeRental.service;

import com.MotorbikeRental.dto.EmailCancelBookingDto;
import com.MotorbikeRental.dto.EmailCancelBookingForLessorDto;
import com.MotorbikeRental.dto.EmailSuccessBookingDto;
import com.MotorbikeRental.dto.EmailSuccessBookingForLessorDto;
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

    String sendEmailSuccessBooking(EmailSuccessBookingDto emailSuccessBookingDto);

    String sendEmailSuccessBookingForLessor(EmailSuccessBookingForLessorDto emailSuccessBookingForLessorDto);

    String sendEmailCancleBooking(EmailCancelBookingDto emailCancelBookingDto);

    String sendEmailCancleBookingForLessor(EmailCancelBookingForLessorDto emailCancelBookingForLessorDto);

    String sendEmailApproveBooking(EmailSuccessBookingDto emailSuccessBookingDto);

    String sendEmailApproveBookingForLessor(EmailSuccessBookingForLessorDto emailSuccessBookingForLessorDto);

    String sendEmailRentingBooking(EmailSuccessBookingDto emailSuccessBookingDto);

    String sendEmailRentingBookingForLessor(EmailSuccessBookingForLessorDto emailSuccessBookingForLessorDto);

    String sendEmailDoneBooking(EmailSuccessBookingDto emailSuccessBookingDto);

    String sendEmailDoneBookingForLessor(EmailSuccessBookingForLessorDto emailSuccessBookingForLessorDto);

    String sendEmailRejectBooking(EmailCancelBookingDto emailCancelBookingDto);

    String sendEmailRejectBookingForLessor(EmailCancelBookingForLessorDto emailCancelBookingForLessorDto);

    String sendEmailDepositMadeBooking(EmailSuccessBookingDto emailSuccessBookingDto);

    String sendEmailDepositMadeBookingForLessor(EmailSuccessBookingForLessorDto emailSuccessBookingForLessorDto);

    String sendEmailDepositNotification(EmailSuccessBookingDto emailSuccessBookingDto);
}
