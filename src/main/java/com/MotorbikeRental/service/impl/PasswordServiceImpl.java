package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.exception.DuplicateUserException;
import com.MotorbikeRental.repository.UserRepository;
import com.MotorbikeRental.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {
    private final UserRepository userRepository;
    @Override
    public void forgotPassword(User user,String password) {
        user.setPassword(password);
        userRepository.save(user);
    }

    @Override
    public void changePassword(User user) {

    }

    @Override
    public boolean checkEmail(String email) {
        if(!userRepository.existsByEmail(email)){
            throw new DuplicateUserException("Email not existed");
        }
        return true;
    }


}
