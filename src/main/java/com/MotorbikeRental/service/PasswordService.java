package com.MotorbikeRental.service;

import com.MotorbikeRental.dto.ChangePasswordRequest;
import com.MotorbikeRental.entity.User;

public interface PasswordService {
    void forgotPassword(User user, String password);

    void changePassword(ChangePasswordRequest changePasswordRequest);

    boolean checkEmail(String email);
}
