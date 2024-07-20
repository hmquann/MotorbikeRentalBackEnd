package com.MotorbikeRental.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
@Data
@Entity
@ToString
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    private LocalDateTime createdAt;

    private String wordFilePath;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
