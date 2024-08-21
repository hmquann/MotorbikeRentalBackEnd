package com.MotorbikeRental.controller;

import com.MotorbikeRental.dto.ChangePasswordRequest;
import com.MotorbikeRental.dto.ResetPasswordRequest;
import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.service.EmailService;
import com.MotorbikeRental.service.PasswordService;
import com.MotorbikeRental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;
    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping (value="/forgot")
    public ResponseEntity<User> forgotPassword(@RequestParam String email){
        passwordService.checkEmail(email);
        User user = userService.getUserByEmail(email);

        //String url = "http://localhost:3000/password/resetnewpassword/"+user.getToken();
        String url = "https://mimotorbe-htccgvbqdzb6h2em.eastus2-01.azurewebsites.net/password/resetnewpassword/"+user.getToken();
        emailService.sendForgotPasswordEmail(user,url);

        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/reset/{token}")
    public ResponseEntity<User> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest, @PathVariable String token){
        User user = userService.getUserByToken(token);
        String password = passwordEncoder.encode(resetPasswordRequest.getPassword());
        passwordService.forgotPassword(user,password);
        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/change")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        passwordService.changePassword(changePasswordRequest);
        return ResponseEntity.ok("done");
    }
}
