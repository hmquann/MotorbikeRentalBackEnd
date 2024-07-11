package com.MotorbikeRental.service;

import com.MotorbikeRental.dto.BookingRequest;
import com.MotorbikeRental.entity.Booking;
import com.MotorbikeRental.entity.Motorbike;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {
    Booking saveBooking(BookingRequest bookingRequest);
    boolean changeStatusBooking();
    ResponseEntity<List<Booking>> getBookingListByMotorbikeId(Long motorbikeId);

    List<BookingRequest> getBookingListByRenterId(Long renterId);
}
