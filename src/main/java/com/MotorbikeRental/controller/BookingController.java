package com.MotorbikeRental.controller;

import com.MotorbikeRental.dto.BookingDto;
import com.MotorbikeRental.dto.BookingRequest;
import com.MotorbikeRental.entity.Booking;
import com.MotorbikeRental.service.BookingService;
import com.MotorbikeRental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    @RequestMapping (value="/order",method =RequestMethod.POST)
    public ResponseEntity<Booking> orderBooking(@RequestBody BookingRequest bookingRequest){
        return ResponseEntity.ok(bookingService.saveBooking(bookingRequest));
    }

    @GetMapping(value="/changestatus")
    public ResponseEntity<Booking> changeStatusBooking(@PathVariable Long userId,@RequestBody Booking booking){
//        bookingService.saveBooking(booking,userId);
//
        return null;
    }
    @GetMapping(value="/listSchedule/{id}")
    public List<BookingDto> getListBookingByMotorbike(@PathVariable Long id){
        return bookingService.getBookingListByMotorbikeId(id);
    }
@PostMapping(value="markBusyDays/{id}")
    public void markBusyDays(@PathVariable Long id,@RequestBody BookingDto bookingDto){
        bookingService.markBusyDays(bookingDto.getStartDate(),bookingDto.getEndTime(),id);
}

}
