package com.MotorbikeRental.controller;

import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verify")
@RequiredArgsConstructor
public class VerifyController {
    private final UserService userService;
    @GetMapping("/{token}")
    public ResponseEntity<?> verifyToken(@PathVariable String token) {

        User user = userService.getUserByToken(token);
        userService.toggleUserActiveStatus(user.getId());
        return ResponseEntity.ok(user);
    }
}
