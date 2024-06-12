package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.entity.ChatMessage;
import com.MotorbikeRental.repository.ChatMessageRepository;
import com.MotorbikeRental.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public ChatMessage saveChatMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    @Override
    public List<ChatMessage> getAllChatMessages() {
        return chatMessageRepository.findAll();
    }

    @Override
    public List<ChatMessage> getChatMessagesByUserId(Long userId) {
        return chatMessageRepository.findByUserId(userId);
    }
}