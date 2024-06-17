package com.MotorbikeRental.service;

import com.MotorbikeRental.dto.BookingRequest;
import com.MotorbikeRental.entity.Booking;
import org.springframework.stereotype.Service;

@Service
public interface BookingService {
    Booking saveBooking(BookingRequest bookingRequest);
    boolean changeStatusBooking();

}
