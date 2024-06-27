package com.MotorbikeRental.service;

import com.MotorbikeRental.dto.JwtAuthenticationResponse;
import com.MotorbikeRental.dto.RefreshTokenRequest;
import com.MotorbikeRental.dto.SigninRequest;
import com.MotorbikeRental.dto.SignupRequest;
import com.MotorbikeRental.entity.User;

public interface AuthenticationService {
     User signUp(SignupRequest signupRequest);

     JwtAuthenticationResponse signin(SigninRequest signinRequest);

     JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

     String checkEmail(String email);
}
