package com.MotorbikeRental.service;


import com.MotorbikeRental.entity.Message;
import com.corundumstudio.socketio.SocketIOClient;

public interface SocketService {

    void sendSocketMessage(SocketIOClient senderClient, Message message, String room);

    void saveMessage(SocketIOClient senderClient, Message message);

    void saveInfoMessage(SocketIOClient senderClient, String message, String room) ;

}

