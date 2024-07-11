package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.dto.*;
import com.MotorbikeRental.entity.Booking;
import com.MotorbikeRental.entity.BookingStatus;
import com.MotorbikeRental.entity.License;
import com.MotorbikeRental.entity.Motorbike;
import com.MotorbikeRental.repository.BookingRepository;
import com.MotorbikeRental.service.BookingService;
import com.MotorbikeRental.service.MotorbikeService;
import com.MotorbikeRental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.MotorbikeRental.entity.BookingStatus.PENDING;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;;
    private final MotorbikeService motorbikeService;
    @Autowired
    private ModelMapper mapper;
    @Override
    public Booking saveBooking(BookingRequest bookingRequest) {
        Booking booking = new Booking();
        Motorbike motorbike = motorbikeService.getMotorbikeById(bookingRequest.getMotorbikeId());
        booking.setStartDate(bookingRequest.getStartDate());
        booking.setEndDate(bookingRequest.getEndDate());
        booking.setRenter(userService.getUserById(bookingRequest.getRenterId()));
        booking.setMotorbike(motorbike);
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

    @Override
    public List<BookingRequest> getBookingListByRenterId(Long renterId) {
        List<Booking> bookingList= bookingRepository.getBookingListByRenterId(renterId);
        List<BookingRequest> bookingRequestList = bookingList.stream()
                .map(booking -> mapper.map(booking, BookingRequest.class))
                .collect(Collectors.toList());
        return bookingRequestList;
    }
}
