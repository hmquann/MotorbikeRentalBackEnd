package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.entity.Message;
import com.MotorbikeRental.entity.MessageType;
import com.MotorbikeRental.service.MessageService;
import com.MotorbikeRental.service.SocketService;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocketServiceImpl implements SocketService {

    private final MessageService messageService;
    @Override
    public void sendSocketMessage(SocketIOClient senderClient, Message message, String room) {
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent("read_message",
                        message);
            }
        }
    }

    @Override
    public void saveMessage(SocketIOClient senderClient, Message message) {
        Message storedMessage = messageService.saveMessage(Message.builder()
                .messageType(MessageType.CLIENT)
                .content(message.getContent())
                .room(message.getRoom())
                .userId(message.getUserId())
                .build());
        sendSocketMessage(senderClient, storedMessage, message.getRoom());
    }

    @Override
    public void saveInfoMessage(SocketIOClient senderClient, String message, String room) {

    }
}
