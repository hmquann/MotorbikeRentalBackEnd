package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.dto.ChangePasswordRequest;
import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.exception.DuplicateUserException;
import com.MotorbikeRental.exception.InvalidCredentialsException;
import com.MotorbikeRental.repository.UserRepository;
import com.MotorbikeRental.service.PasswordService;
import com.MotorbikeRental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    @Override
    public void forgotPassword(User user,String password) {
        user.setPassword(password);
        userRepository.save(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = userService.getUserByToken(changePasswordRequest.getToken());
        String oldPassword = changePasswordRequest.getOldPassword();
        String newPassword = changePasswordRequest.getNewPassword();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidCredentialsException("Old password not correct");
        }
        else{
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }

    }

    @Override
    public boolean checkEmail(String email) {
        if(!userRepository.existsByEmail(email)){
            throw new DuplicateUserException("Email not existed");
        }
        return true;
    }


}
