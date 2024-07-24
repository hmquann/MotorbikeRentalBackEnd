package com.MotorbikeRental.controller;


import com.MotorbikeRental.dto.*;
import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.service.AuthenticationService;
import com.MotorbikeRental.service.EmailService;
import com.MotorbikeRental.service.UserService;
import com.MotorbikeRental.service.impl.EmailServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {


    private final AuthenticationService authenticationService;
    private final EmailService emailService;
    private final UserService userService;

    @CrossOrigin(origins = "https://proud-rock-0ffde1d0f.5.azurestaticapps.net", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
    @RequestMapping (value="/signup",method =RequestMethod.POST)
    public ResponseEntity<User> signUp(@RequestBody SignupRequest signupRequest, HttpServletRequest httpServletRequest){
        User user = authenticationService.signUp(signupRequest);
        String url = httpServletRequest.getRequestURL().toString()+"/verify/"+user.getToken();
        String newUrl = url.replace("rentalmotorbikebe.azurewebsites.net", "proud-rock-0ffde1d0f.5.azurestaticapps.net");
        emailService.sendVerificationEmail(user, newUrl.replace(httpServletRequest.getServletPath(),""));
        return ResponseEntity.ok(user);

    }

    @RequestMapping (value="/changeEmail",method =RequestMethod.POST)
    public ResponseEntity<User> changeEmail(@RequestBody ChangeEmailRequest changeEmailRequest, HttpServletRequest httpServletRequest){
        User user = userService.getUserById(changeEmailRequest.getUserId());
        String newEmail = changeEmailRequest.getNewEmail();
        authenticationService.checkEmail(newEmail);
        String url = httpServletRequest.getRequestURL().toString()+"/updateEmail/"+user.getToken()+"/"+newEmail;
        String newUrl = url.replace("rentalmotorbikebe.azurewebsites.net", "proud-rock-0ffde1d0f.5.azurestaticapps.net");
        emailService.sendChangeEmail(user, newUrl.replace(httpServletRequest.getServletPath(),""), newEmail);
        return ResponseEntity.ok(user);

    }

    @CrossOrigin
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody SigninRequest signinRequest){

            return ResponseEntity.ok(authenticationService.signin(signinRequest));
    }


    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }

    public ResponseEntity<JwtAuthenticationResponse> demo(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }


}
