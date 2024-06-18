package com.MotorbikeRental.controller;


import com.MotorbikeRental.dto.JwtAuthenticationResponse;
import com.MotorbikeRental.dto.RefreshTokenRequest;
import com.MotorbikeRental.dto.SigninRequest;
import com.MotorbikeRental.dto.SignupRequest;
import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.service.AuthenticationService;
import com.MotorbikeRental.service.EmailService;
import com.MotorbikeRental.service.impl.EmailServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {


    private final AuthenticationService authenticationService;
    private final EmailService emailService;


    @RequestMapping (value="/signup",method =RequestMethod.POST)
    public ResponseEntity<User> signUp(@RequestBody SignupRequest signupRequest, HttpServletRequest httpServletRequest){
        User user = authenticationService.signUp(signupRequest);
        String url = httpServletRequest.getRequestURL().toString()+"/verify/"+user.getToken();
        String newUrl = url.replace("localhost:8080", "localhost:3000");
        emailService.sendVerificationEmail(user, newUrl.replace(httpServletRequest.getServletPath(),""));
        return ResponseEntity.ok(user);

    }

    @CrossOrigin("*")
    @PostMapping("/signin")
    public ResponseEntity<?> signin( @Valid @RequestBody SigninRequest signinRequest){
//        try {
//        System.out.println(accessToken);

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
