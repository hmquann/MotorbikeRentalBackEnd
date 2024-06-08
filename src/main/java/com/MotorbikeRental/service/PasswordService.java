package com.MotorbikeRental.service;

import com.MotorbikeRental.entity.User;

public interface PasswordService {
    void forgotPassword(User user, String password);

    void changePassword(User user);

    boolean checkEmail(String email);
}
