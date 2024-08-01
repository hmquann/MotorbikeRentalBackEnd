package com.MotorbikeRental.dto;

import com.MotorbikeRental.entity.BookingStatus;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BookingDto implements Serializable {
    private LocalDateTime startDate;
    private LocalDateTime endTime;
    private BookingStatus status;
    private double totalPrice;
}