package com.MotorbikeRental.controller;
import com.MotorbikeRental.dto.*;
import com.MotorbikeRental.entity.*;
import com.MotorbikeRental.repository.UserRepository;
import com.MotorbikeRental.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import com.MotorbikeRental.entity.Motorbike;
import com.MotorbikeRental.service.MotorbikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/motorbike")
public class MotorbikeController {

    @Autowired
    private MotorbikeService motorbikeService;

    @Autowired
    private  JWTService jwtService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/pending")
    public List<Motorbike> getPendingMotorbikes(){
        return motorbikeService.getPendingMotorbikes();
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<Motorbike> approveMotorbike(@PathVariable Long id) {
        Motorbike approvedMotorbike = motorbikeService.approveMotorbike(id);
        return ResponseEntity.ok(approvedMotorbike);
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<Motorbike> rejectMotorbike(@PathVariable Long id) {
        Motorbike approvedMotorbike = motorbikeService.rejectMotorbike(id);
        return ResponseEntity.ok(approvedMotorbike);
    }



    @RequestMapping (value="/register",method =RequestMethod.POST)
    public ResponseEntity<Motorbike> registerMotorbike(@RequestHeader("Authorization") String accessToken, @RequestBody Motorbike motorbike){
        String token = accessToken.split(" ")[1];
        String username = this.jwtService.extractUsername(token);
        System.out.println(username);
        Optional<User> user = userRepository.findByEmail(username);
        if(user.isPresent()){
            user.get().setBalance(0.0);
            motorbike.setUser(user.get());
        }

        Motorbike newMotor = motorbikeService.registerMotorbike(motorbike);

        return ResponseEntity.ok(newMotor);
    }
}




