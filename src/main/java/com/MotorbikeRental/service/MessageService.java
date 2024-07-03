package com.MotorbikeRental.service;

import com.MotorbikeRental.entity.Message;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {
    List<Message> getMessagesByRoom(String room);
    Message saveMessage(Message message);
    List<Message> getAllMessagesByUser(String room);

    List<Message> getListMessagesByUniqueRoom(String room);

    Message getLastMessageByUniqueRoom(String uniqueRoom);

    Message getLastMessageAllRoom(String room);
}
