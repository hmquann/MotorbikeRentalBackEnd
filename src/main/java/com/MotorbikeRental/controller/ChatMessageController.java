package com.MotorbikeRental.controller;

import com.MotorbikeRental.entity.ChatMessage;
import com.MotorbikeRental.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;


    @PostMapping("/send")
    public ResponseEntity<ChatMessage> sendMessage(@RequestBody ChatMessage chatMessage) {
        ChatMessage savedMessage = chatMessageService.saveChatMessage(chatMessage);
        return ResponseEntity.ok(savedMessage);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ChatMessage>> getAllMessages() {
        List<ChatMessage> messages = chatMessageService.getAllChatMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatMessage>> getMessagesByUserId(@PathVariable Long userId) {
        List<ChatMessage> messages = chatMessageService.getChatMessagesByUserId(userId);
        return ResponseEntity.ok(messages);
    }
}