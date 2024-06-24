package com.MotorbikeRental.controller;

import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/updateEmail")
@RequiredArgsConstructor
public class EmailController {
    private final UserService userService;
    @PostMapping(value="/{token}/{newEmail}")
    public ResponseEntity<User> updateEmail(@PathVariable String token, @PathVariable String newEmail){
        User user = userService.getUserByToken(token);
        userService.updateUserEmail(user.getId(), newEmail);
        return ResponseEntity.ok(user);
    }

}
