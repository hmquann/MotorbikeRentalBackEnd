package com.MotorbikeRental.service.impl;


import com.MotorbikeRental.config.VNPayConfig;
import com.MotorbikeRental.dto.PaymentDto;
import com.MotorbikeRental.entity.Role;
import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.exception.UserNotFoundException;
import com.MotorbikeRental.repository.RoleRepository;
import com.MotorbikeRental.repository.UserRepository;
import com.MotorbikeRental.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Autowired
    private final VNPayConfig vnpayConfig;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository.findByEmailOrPhone(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                        if(!user.isActive()){
                            throw new LockedException("User is locked");
                        }
                        return user;
            }

        };
    }

    public void toggleUserActiveStatus(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setActive(!user.isActive());
        userRepository.save(user);
    }


    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public User getUserByToken(String token) {
        return userRepository.findByToken(token)
                .orElseThrow(() -> new UserNotFoundException("User with token " + token + " not found"));
    }

    @Override
    public User updateUser(Long id, User user) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        if (optionalUser.isPresent()) {
            User existUser = optionalUser.get();

            existUser.setFirstName(user.getFirstName());
            existUser.setLastName(user.getLastName());
            existUser.setEmail(user.getEmail());
            existUser.getRoles().clear();
            for (Role role : user.getRoles()) {
                // Kiểm tra xem vai trò đã tồn tại chưa
                Role existingRole = roleRepository.findByName(role.getName());
                if (existingRole != null) {
                    existUser.getRoles().add(existingRole);
                } else {
                    // Nếu vai trò chưa tồn tại, thêm vai trò mới vào cơ sở dữ liệu
                    roleRepository.save(role);
                    existUser.getRoles().add(role);
                }
            }
            return userRepository.save(existUser);

        }
        return null;

    }

    @Override
    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new EntityNotFoundException("User with ID "+ id + " not found");
        }
        userRepository.deleteById(id);
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public void updateUserBalance(Long userId, double amount) {
        // Kiểm tra xem userId có tồn tại hay không
//        Optional<User> optionalUser = userRepository.findById(userId);
//        if (optionalUser.isPresent()) {
//            User user = optionalUser.get();
//            // Kiểm tra xem balance có phải là null không trước khi gán
//            Double currentBalance = user.getBalance();
//            if (currentBalance != null) {
//                // Thực hiện cập nhật balance
//                double newBalance = currentBalance + amount;
//                user.setBalance(newBalance);
//                userRepository.save(user);
//            } else {
//                // Nếu balance là null, bạn có thể gán một giá trị mặc định hoặc xử lý theo cách khác
//                // Ví dụ: user.setBalance(amount);
//            }
//        } else {
//            // Xử lý khi không tìm thấy userId
//            System.out.println("User with ID " + userId + " not found.");
//        }
    }
}
