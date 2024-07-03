package com.MotorbikeRental.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BookingDto implements Serializable {
    private LocalDateTime startDate;
    private LocalDateTime endTime;
}