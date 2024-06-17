package com.MotorbikeRental.service;

import com.MotorbikeRental.entity.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    ChatMessage saveChatMessage(ChatMessage chatMessage);
    List<ChatMessage> getAllChatMessages();
    List<ChatMessage> getChatMessagesByUserId(Long userId);
}