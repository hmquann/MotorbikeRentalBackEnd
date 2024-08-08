package com.MotorbikeRental.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmailSuccessBookingForLessorDto {
    String lessorName;
    String lessorEmail;
    String renterName;
    String motorbikeName;
    String motorbikePlate;
    LocalDateTime bookingTime;
    LocalDateTime startDate;
    LocalDateTime endDate;
    double totalPrice;
    String receiveLocation;
}
