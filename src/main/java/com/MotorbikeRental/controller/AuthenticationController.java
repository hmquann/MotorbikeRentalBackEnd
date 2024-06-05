package com.MotorbikeRental.controller;


import com.MotorbikeRental.dto.JwtAuthenticationResponse;
import com.MotorbikeRental.dto.RefreshTokenRequest;
import com.MotorbikeRental.dto.SigninRequest;
import com.MotorbikeRental.dto.SignupRequest;
import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {


    private final AuthenticationService authenticationService;

    @RequestMapping (value="/signup",method =RequestMethod.POST)
    public ResponseEntity<User> signUp(@RequestBody SignupRequest signupRequest){
        User user = authenticationService.signUp(signupRequest);
        return ResponseEntity.ok(user);
    }

    @CrossOrigin
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody SigninRequest signinRequest){
//        try {
            return ResponseEntity.ok(authenticationService.signin(signinRequest));
//        } catch (InvalidCredentialsException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password or email");
//        }
    }


    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }



}
