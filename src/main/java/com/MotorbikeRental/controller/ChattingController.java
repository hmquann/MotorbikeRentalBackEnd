package com.MotorbikeRental.controller;

import com.MotorbikeRental.dto.ChattingDto;
import com.MotorbikeRental.dto.DiscountDtoResponse;
import com.MotorbikeRental.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatting")
@RequiredArgsConstructor
public class ChattingController {

    private final ChattingService chattingService;


    @PostMapping("/create")
    public ResponseEntity<ChattingDto> getDiscountById(@RequestBody ChattingDto chattingDto) {
        return ResponseEntity.ok(chattingService.createChatting(chattingDto));
    }

    @GetMapping("/getUser/{userEmail}")
    public ResponseEntity<List<ChattingDto>> getUsersByEmail(@PathVariable String userEmail) {
        return ResponseEntity.ok(chattingService.findByUserEmail(userEmail));
    }
}
