package com.MotorbikeRental.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class  DepositTimeDto {
    Long bookingId;
    LocalDateTime depositTime;
}
