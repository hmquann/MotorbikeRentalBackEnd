package com.MotorbikeRental.service.impl;


import com.MotorbikeRental.config.VNPayConfig;
import com.MotorbikeRental.dto.PaymentDto;
import com.MotorbikeRental.dto.RegisterMotorbikeDto;
import com.MotorbikeRental.dto.UserDto;
import com.MotorbikeRental.entity.*;
import com.MotorbikeRental.exception.UserNotFoundException;
import com.MotorbikeRental.repository.RoleRepository;
import com.MotorbikeRental.repository.TransactionRepository;
import com.MotorbikeRental.repository.UserRepository;
import com.MotorbikeRental.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Autowired
    private final VNPayConfig vnpayConfig;

    @Autowired
    private final TransactionRepository transactionRepository;
    @Autowired
    private final ModelMapper mapper;
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository.findByEmailOrPhone(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                if (!user.isActive()) {
                    throw new LockedException("User is locked");
                }
                return user;
            }

        };
    }


    @Override
    public void activeUser(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setActive(true);
        userRepository.save(user);
    }
    public void toggleUserActiveStatus(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setActive(!user.isActive());
        userRepository.save(user);
    }



    @Override
    public UserDto getUserDtoById(Long id) {
        User user= userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        return mapper.map(user,UserDto.class);
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }

    @Override
    public UserDto getUserDtoByEmail(String email) {
         User user=userRepository.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
      return mapper.map(user,UserDto.class);
    }

    @Override
    public User getUserByEmail(String email) {
        User user=userRepository.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
        return user;
    }

    @Override
    public UserDto getUserDtoByToken(String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new UserNotFoundException("User with token " + token + " not found"));
        return mapper.map(user, UserDto.class);
    }

    @Override
    public User getUserByToken(String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new UserNotFoundException("User with token " + token + " not found"));
        return user;
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
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    public Page<UserDto> getAllUser(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<User> userList = userRepository.findAll();
        List<UserDto> dtoList = userList.stream()
                .map(user -> mapper.map(user, UserDto.class))
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, dtoList.size());
    }

    public void updateUserBalance(Long userId, BigDecimal amount) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            BigDecimal currentBalance = user.getBalance();
            if (currentBalance != null) {

                BigDecimal newBalance = currentBalance.add(amount);
                user.setBalance(newBalance);
                userRepository.save(user);
            } else {
                // Nếu balance là null, bạn có thể gán một giá trị mặc định hoặc xử lý theo cách khác
                // Ví dụ: user.setBalance(amount);
            }
        } else {
            // Xử lý khi không tìm thấy userId
            System.out.println("User with ID " + userId + " not found.");
        }

    }

    @Override

    public void withdrawMoney(Long userId, BigDecimal amount) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            BigDecimal currentBalance = user.getBalance();
            if (currentBalance.compareTo(amount) < 0) {
                throw new Exception("Insufficient money");
            }
            BigDecimal newBalance = currentBalance.subtract(amount);
            user.setBalance(newBalance);
            userRepository.save(user);

            Transaction transaction = new Transaction();
            transaction.setUsers(user);
            transaction.setAmount(amount);
            transaction.setProcessed(true);
            transaction.setTransactionDate(new Date());
            transaction.setType(TransactionType.WITHDRAW);
            transaction.setStatus(TransactionStatus.SUCCESS);
            transactionRepository.save(transaction);
        }
    }
    public void activeUserStatus(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void updateUserEmail(Long id, String email) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setEmail(email);
        userRepository.save(user);
    }
}
