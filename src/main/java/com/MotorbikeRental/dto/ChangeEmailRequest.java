package com.MotorbikeRental.dto;

import lombok.Data;

@Data
public class ChangeEmailRequest {
    private Long userId;
    private String newEmail;
}
