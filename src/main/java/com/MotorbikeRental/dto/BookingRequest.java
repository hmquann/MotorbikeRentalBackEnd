package com.MotorbikeRental.dto;

import com.MotorbikeRental.entity.BookingStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequest {
    private Long bookingId;
    private Long renterId;
    private Long motorbikeId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime bookingTime;
    private double totalPrice;
    private String receiveLocation;
    private BookingStatus status;
    private boolean isFeedbackkk;
    private Double longitude;
    private Double latitude;
}
