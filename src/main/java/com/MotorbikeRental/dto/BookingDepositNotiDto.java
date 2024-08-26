package com.MotorbikeRental.dto;

import com.MotorbikeRental.entity.BookingStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDepositNotiDto {
    Long bookingId;
    LocalDateTime depositTime;
    boolean depositNoti;
    boolean depositCanceled;
    BookingStatus bookingStatus;
}
