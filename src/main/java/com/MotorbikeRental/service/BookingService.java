package com.MotorbikeRental.service;

import com.MotorbikeRental.dto.BookingRequest;
import com.MotorbikeRental.dto.FilterBookingDto;
import com.MotorbikeRental.entity.Booking;
import com.MotorbikeRental.entity.Motorbike;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {
    Booking  saveBooking(BookingRequest bookingRequest);
    Booking changeStatusBooking(Long id,String status);
    ResponseEntity<List<Booking>> getBookingListByMotorbikeId(Long motorbikeId);

    List<BookingRequest> getBookingListByRenterId(Long renterId);

    List<BookingRequest> getBookingListByLessorId(Long lessorId);

    public List<BookingRequest> filterBookings(FilterBookingDto filterBookingDto);

    boolean hasFeedbackBeenSent(Long bookingId);


}
