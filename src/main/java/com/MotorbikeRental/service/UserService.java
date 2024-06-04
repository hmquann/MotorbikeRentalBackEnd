package com.MotorbikeRental.service;

import com.MotorbikeRental.dto.PaymentDto;
import com.MotorbikeRental.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserDetailsService userDetailsService();
    User getUserById(Long id);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    List<User> getAllUser();
    void toggleUserStatus(Long id);
    void updateUserBalance(Long id, double balance);

}
