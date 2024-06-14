package com.MotorbikeRental.controller;


import com.MotorbikeRental.entity.Motorbike;
import com.MotorbikeRental.service.MotorbikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/motorbike")
public class MotorbikeController {

    @Autowired
    private MotorbikeService motorbikeService;

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
}
