package com.MotorbikeRental.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDepositNotiDto {
    Long bookingId;
    LocalDateTime depositTime;
    boolean depositNoti;
    boolean depositCanceled;
}
