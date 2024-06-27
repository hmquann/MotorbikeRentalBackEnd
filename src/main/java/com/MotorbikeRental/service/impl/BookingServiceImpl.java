package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.dto.BookingRequest;
import com.MotorbikeRental.entity.Booking;
import com.MotorbikeRental.entity.BookingStatus;
import com.MotorbikeRental.entity.Motorbike;
import com.MotorbikeRental.repository.BookingRepository;
import com.MotorbikeRental.service.BookingService;
import com.MotorbikeRental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.MotorbikeRental.entity.BookingStatus.PENDING;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;;
    @Override
    public Booking saveBooking(BookingRequest bookingRequest) {
        Booking booking = new Booking();
        booking.setStartDate(bookingRequest.getStartDate());
        booking.setEndDate(bookingRequest.getEndDate());
        booking.setRenter_id(bookingRequest.getRenterId());
        booking.setMotorbikeId(1L);
        booking.setReceiveLocation(bookingRequest.getReceiveLocation());
        booking.setTotalPrice(bookingRequest.getTotalPrice());
        booking.setStatus(PENDING);

        return bookingRepository.save(booking);
    }

    @Override
    public boolean changeStatusBooking() {
        return false;
    }

    @Override
    public ResponseEntity<List<Booking>> getBookingListByMotorbikeId(Long motorbikeId) {
        return  bookingRepository.getBookingByMotorbikeId(motorbikeId);
    }
}
