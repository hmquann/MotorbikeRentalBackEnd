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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/motorbike")
@RequiredArgsConstructor
public class MotorbikeController {

    private final MotorbikeService motorbikeService;

    @Autowired
    private  BrandService brandService;

    @Autowired
    private ModelService modelService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepository userRepository;

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
    @GetMapping("/brandList")
    public List<Brand>getAllBrand(){
        return brandService.getAllBrand();
    }
    @GetMapping("/modelList")
    public List<Model>getAllModel(){
        return modelService.getAllModel();
    }
    }


