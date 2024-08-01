package com.MotorbikeRental.controller;

import com.MotorbikeRental.dto.*;
import com.MotorbikeRental.entity.Booking;
import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.service.BookingService;
import com.MotorbikeRental.service.EmailService;
import com.MotorbikeRental.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;
    private final EmailService emailService;

    @RequestMapping (value="/create",method =RequestMethod.POST)
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest){
        return ResponseEntity.ok(bookingService.saveBooking(bookingRequest));
    }

    @PutMapping(value="/changeStatus/{id}/{status}")
    public ResponseEntity<Booking> changeStatusBooking(@PathVariable Long id,@PathVariable String status){
        return ResponseEntity.ok(bookingService.changeStatusBooking(id,status));
    }
    @GetMapping(value="/listSchedule/{id}")
    public List<BookingDto> getListBookingByMotorbike(@PathVariable Long id){
        return bookingService.getBookingListByMotorbikeId(id);
    }
@PostMapping(value="markBusyDays/{id}")
    public void markBusyDays(@PathVariable Long id,@RequestBody BookingDto bookingDto){
        bookingService.markBusyDays(bookingDto.getStartDate(),bookingDto.getEndTime(),id);
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

    @GetMapping("/checkFeedbackStatus/{bookingId}")
    public ResponseEntity<Map<String, Boolean>> checkFeedbackStatus(@PathVariable Long bookingId) {
        boolean feedbackSent = bookingService.hasFeedbackBeenSent(bookingId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("feedbackSent", feedbackSent);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/motorbike/{motorbikeId}")
    public ResponseEntity<List<BookingRequest>> getBookingsByMotorbikeId(@PathVariable Long motorbikeId) {
        List<BookingRequest> bookings = bookingService.findByMotorbikeId(motorbikeId);
        return ResponseEntity.ok(bookings);
    }


    @RequestMapping (value="/sendEmailSuccessBooking",method =RequestMethod.POST)
    public String sendEmailSuccessBooking(@RequestBody EmailSuccessBookingDto emailSuccessBookingDto){
        return emailService.sendEmailSuccessBooking(emailSuccessBookingDto);
    }
}
