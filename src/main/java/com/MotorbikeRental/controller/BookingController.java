package com.MotorbikeRental.controller;

import com.MotorbikeRental.dto.BookingDto;
import com.MotorbikeRental.dto.BookingRequest;
import com.MotorbikeRental.dto.FilterBookingDto;
import com.MotorbikeRental.entity.Booking;
import com.MotorbikeRental.service.BookingService;
import com.MotorbikeRental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    @RequestMapping (value="/create",method =RequestMethod.POST)
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest){
        return ResponseEntity.ok(bookingService.saveBooking(bookingRequest));
    }

    @GetMapping(value="/changestatus")
    public ResponseEntity<Booking> changeStatusBooking(@PathVariable Long userId,@RequestBody Booking booking){
//        bookingService.saveBooking(booking,userId);
//
        return null;
    }
    @GetMapping(value="/listSchedule/{id}")
    public ResponseEntity<List<Booking>> getListBookingByMotorbike(@PathVariable Long id){
        return bookingService.getBookingListByMotorbikeId(id);
    }

    @GetMapping(value = "/getListBookingByRenterId/{id}")
    public ResponseEntity<List<BookingRequest>> getListBookingByRenterId(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.getBookingListByRenterId(id));
    }

    @GetMapping(value = "/getListBookingByLessorId/{id}")
    public ResponseEntity<List<BookingRequest>> getListBookingByLessorId(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.getBookingListByLessorId(id));
    }


    @PostMapping("/filter")
    public ResponseEntity<List<BookingRequest>> filterBookings(@RequestBody FilterBookingDto filterBookingDto) {
        return ResponseEntity.ok(bookingService.filterBookings(filterBookingDto));
    }
}
