package com.MotorbikeRental.service;

import com.MotorbikeRental.dto.ChattingDto;
import com.MotorbikeRental.entity.Chatting;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChattingService {
    ChattingDto createChatting(ChattingDto chattingDto);

    List<ChattingDto> findByUserEmail(String userEmail);
}
