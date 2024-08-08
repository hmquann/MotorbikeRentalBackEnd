package com.MotorbikeRental.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmailCancelBookingDto {
    String renterName;
    String renterEmail;
    String motorbikeName;
    String motorbikePlate;
    LocalDateTime bookingTime;
    LocalDateTime startDate;
    LocalDateTime endDate;
    double totalPrice;
    String receiveLocation;
    String reason;
}
