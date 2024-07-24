package com.MotorbikeRental.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Long userId;
}
