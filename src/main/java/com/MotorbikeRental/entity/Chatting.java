package com.MotorbikeRental.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "Chatting")
@ToString
public class Chatting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @Column(name = "email_user1")
    private String emailUser1;

    @Column(name = "email_user2")
    private String emailUser2;

}