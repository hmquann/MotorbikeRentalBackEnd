package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.dto.BookingRequest;
import com.MotorbikeRental.dto.ChattingDto;
import com.MotorbikeRental.dto.LicenseDto;
import com.MotorbikeRental.entity.Chatting;
import com.MotorbikeRental.exception.ExistChattingException;
import com.MotorbikeRental.repository.ChattingRepository;
import com.MotorbikeRental.service.ChattingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChattingServiceImpl implements ChattingService {

    @Autowired
    private ChattingRepository chattingRepository;
    @Autowired
    private ModelMapper mapper;
    @Override
    public ChattingDto createChatting(ChattingDto chattingDto) {
        Chatting chatting = new Chatting();
        String email1 = chattingDto.getEmailUser1();
        String email2 = chattingDto.getEmailUser2();
        if (email1.compareTo(email2) < 0) {
            chatting.setEmailUser1(email1);
            chatting.setEmailUser2(email2);
        } else {
            chatting.setEmailUser1(email2);
            chatting.setEmailUser2(email1);
        }

        Chatting existingChatting = chattingRepository.findChattingByEmailUser1AndEmailUser2(chatting.getEmailUser1(), chatting.getEmailUser2());

        if (existingChatting != null) {
            // Nếu đã tồn tại, ném ngoại lệ
            return mapper.map(existingChatting, ChattingDto.class);
        } else {
            // Nếu chưa tồn tại, lưu cuộc trò chuyện mới
            Chatting savedChatting = chattingRepository.save(chatting);
            return mapper.map(savedChatting, ChattingDto.class);
        }
    }

    @Override
    public List<ChattingDto> findByUserEmail(String userEmail) {
        List<Chatting> chattingList = chattingRepository.findByUserEmail(userEmail);
        List<ChattingDto> chattingDtoList = chattingList.stream().map(chatting -> mapper.map(chatting, ChattingDto.class))
                .collect(Collectors.toList());
        return chattingDtoList;
    }

}
