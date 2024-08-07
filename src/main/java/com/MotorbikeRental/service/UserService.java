package com.MotorbikeRental.service;


import com.MotorbikeRental.dto.UserDto;
import com.MotorbikeRental.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface UserService {
    UserDetailsService userDetailsService();
    UserDto getUserDtoById(Long id);
    UserDto getAdmin();
    User getUserById(Long id);
    UserDto getUserDtoByEmail(String email);
    User getUserByEmail(String email);
    UserDto getUserDtoByToken(String token);
    User getUserByToken(String token);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    List<User> getAllUser();
    Page<User> getUserByPagination(int page, int pageSize);
    Page<UserDto> getAllUser(int page, int pageSize);
    void toggleUserActiveStatus(Long id);

    void activeUser(Long id);

    void updateUserBalance(Long id, BigDecimal balance);
    void withdrawMoney(Long userId, BigDecimal amount) throws Exception;
    void subtractMoney(Long userId, BigDecimal amount) throws Exception;
    void addMoney(Long userId, BigDecimal amount) throws Exception;
    void activeUserStatus(Long id);

    void updateUserEmail(Long id, String email);
    Page<UserDto> searchUserByEmailOrPhone(String searchTerm, int page, int size);
    UserDto convertToDto(User user);

    void addLessor(User user);

    String getUserNameByEmail(String email);

    UserDto updateUserNotifications(Long userId, Boolean systemNoti, Boolean emailNoti, Boolean minimizeNoti);
}
