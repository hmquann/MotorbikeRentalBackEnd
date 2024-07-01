package com.MotorbikeRental.controller;

import com.MotorbikeRental.entity.Message;
import com.MotorbikeRental.service.MessageService;
import com.MotorbikeRental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@CrossOrigin // Allow CORS for all origins
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    @GetMapping("/{room}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable String room) {
        return ResponseEntity.ok(messageService.getMessagesByRoom(room));
    }

    @GetMapping("/getUserNameByEmail/{email}")
    public String getUserNameByEmail(@PathVariable String email) {
        return userService.getUserNameByEmail(email);
    }

    @GetMapping("/getAllMessageByUser/{room}")
    public ResponseEntity<List<Message>> getAllMessagesByUser(@PathVariable String room) {
        String roomPattern = "%"+room+"%";
        return ResponseEntity.ok(messageService.getAllMessagesByUser(roomPattern));
    }

    @GetMapping("/getLastMessageByUniqueRoom/{uniqueRoom}")
    public Message getLastMessageByUniqueRoom(@PathVariable String uniqueRoom) {
        return ResponseEntity.ok(messageService.getLastMessageByUniqueRoom(uniqueRoom)).getBody();
    }

    @GetMapping("/getLastMessageAllRoom/{room}")
    public Message getLastMessageAllRoom(@PathVariable String room) {
        String roomPattern = "%"+room+"%";
        return ResponseEntity.ok(messageService.getLastMessageAllRoom(roomPattern)).getBody();
    }

    @GetMapping("/getListMessageByUniqueRoom/{uniqueRoom}")
    public ResponseEntity<List<Message>> getListMessageByUniqueRoom(@PathVariable String uniqueRoom) {
        return ResponseEntity.ok(messageService.getListMessagesByUniqueRoom(uniqueRoom));
    }
    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        return ResponseEntity.ok(messageService.saveMessage(message));
    }
}