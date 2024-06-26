package com.MotorbikeRental.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequest {
    private Long renterId;
    private Long motorbikeId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double totalPrice;
    private String receiveLocation;
}
