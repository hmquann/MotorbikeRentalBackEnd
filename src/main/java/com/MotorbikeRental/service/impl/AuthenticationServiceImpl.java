package com.MotorbikeRental.service.impl;


import com.MotorbikeRental.dto.*;
import com.MotorbikeRental.entity.Role;
import com.MotorbikeRental.exception.DuplicateUserException;
import com.MotorbikeRental.exception.InactiveUserException;
import com.MotorbikeRental.exception.InvalidCredentialsException;
import com.MotorbikeRental.repository.LicenseRepository;
import com.MotorbikeRental.repository.RoleRepository;
import com.MotorbikeRental.repository.UserRepository;
import com.MotorbikeRental.service.AuthenticationService;
import com.MotorbikeRental.service.JWTService;
import com.MotorbikeRental.service.UserService;
import lombok.RequiredArgsConstructor;
import com.MotorbikeRental.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.Hibernate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JWTService jwtService;
    private final RoleRepository roleRepository;
    @Override
    public User signUp(SignupRequest signupRequest){
        if(userRepository.existsByEmail(signupRequest.getEmail())){
            throw new DuplicateUserException("Email existed");
        }

        if(userRepository.existsByPhone(signupRequest.getPhone())){
            throw new DuplicateUserException("Phone existed");
        }


        User user = new User();

        user.setEmail(signupRequest.getEmail());
        user.setFirstName(signupRequest.getFirstname());
        user.setLastName(signupRequest.getLastname());
        user.setPhone(signupRequest.getPhone());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setGender(signupRequest.isGender());
        user.setEmailNoti(true);
        user.setSystemNoti(true);
        user.setMinimizeNoti(true);
        user.setBalance(BigDecimal.valueOf(0.00));
        user.setActive(false);

        Role defaultRole = roleRepository.findByName("USER");
        if (defaultRole == null) {
            defaultRole = new Role("USER");
            roleRepository.save(defaultRole);
        }

        user.getRoles().add(defaultRole);

        String randomCode = RandomStringUtils.randomAlphanumeric(64);
        user.setToken(randomCode);
        user.setActive(false);
        return userRepository.save(user);

    }


    @Override
    public JwtAuthenticationResponse signin(SigninRequest signinRequest) {
        User user = userRepository.findByEmailOrPhone(signinRequest.getEmailOrPhone()).orElseThrow(
                () -> new InvalidCredentialsException("Invalid email ")
        );
        if (!user.isActive()) {
            throw new InactiveUserException("User is inactive");
        }
        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Password not correct");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    signinRequest.getEmailOrPhone(), signinRequest.getPassword()
            ));
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }



        Hibernate.initialize(user.getRoles());


        String jwt = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        Set<Role> roles = user.getRoles();
        List<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toList());

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        UserDto userDto = userService.convertToDto(user);

        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        jwtAuthenticationResponse.setRoles(roleNames);
        jwtAuthenticationResponse.setUserToken(user.getToken());
        jwtAuthenticationResponse.setId(user.getId());
        jwtAuthenticationResponse.setBalance(user.getBalance());
        jwtAuthenticationResponse.setFirstName(user.getFirstName());
        jwtAuthenticationResponse.setLastName(user.getLastName());
        jwtAuthenticationResponse.setGender(user.isGender());
        jwtAuthenticationResponse.setEmailNoti(user.isEmailNoti());
        jwtAuthenticationResponse.setSystemNoti(user.isSystemNoti());
        jwtAuthenticationResponse.setMinimizeNoti(user.isMinimizeNoti());
        jwtAuthenticationResponse.setEmail(user.getEmail());
        jwtAuthenticationResponse.setPhone(user.getPhone());
        jwtAuthenticationResponse.setTotalTripCount(userDto.getTotalTripCount());
        return jwtAuthenticationResponse;
    }


    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        String userEmail = jwtService.extractUsername(refreshTokenRequest.getToken());
        User user = userRepository.findByEmailOrPhone(userEmail).orElseThrow();
        if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user)){
            var jwt = jwtService.generateToken(user);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());

            return jwtAuthenticationResponse;
        }
        return null;
    }

    @Override
    public String checkEmail(String email) {
        if(userRepository.existsByEmail(email)){
            throw new DuplicateUserException("Email existed");
        }
        return email;
    }


}
