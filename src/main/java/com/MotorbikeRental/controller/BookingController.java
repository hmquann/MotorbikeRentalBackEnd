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

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public ResponseEntity<String> createBooking(@RequestBody BookingRequest bookingRequest){
        return ResponseEntity.ok(bookingService.saveBooking(bookingRequest));
    }

    @PutMapping(value="/changeStatus/{id}/{status}")
    public ResponseEntity<String> changeStatusBooking(@PathVariable Long id,@PathVariable String status){
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
@PostMapping(value="/markAvailableDays/{id}")
public void markAvailableDays(@PathVariable Long id,@RequestBody BookingDto bookingDto){
     bookingService.markAvailableDays(bookingDto.getStartDate(),bookingDto.getEndTime(),id);
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

    @GetMapping("/getListUserFromBookingToChat/{userId}")
    public ResponseEntity<List<UserToChat>> getListUserFromBookingToChat(@PathVariable Long userId) {
        List<UserToChat> userToChatList = bookingService.getListUserFromBookingToChat(userId);
        return ResponseEntity.ok(userToChatList);
    }


    @RequestMapping (value="/sendEmailSuccessBooking",method =RequestMethod.POST)
    public String sendEmailSuccessBooking(@RequestBody EmailSuccessBookingDto emailSuccessBookingDto){
        return emailService.sendEmailSuccessBooking(emailSuccessBookingDto);
    }

    @RequestMapping (value="/sendEmailSuccessBookingForLessor",method =RequestMethod.POST)
    public String sendEmailSuccessBookingForLessor(@RequestBody EmailSuccessBookingForLessorDto emailSuccessBookingForLessorDto){
        return emailService.sendEmailSuccessBookingForLessor(emailSuccessBookingForLessorDto);
    }

    @RequestMapping (value="/sendEmailCancelBooking",method =RequestMethod.POST)
    public String sendEmailCancelBooking(@RequestBody EmailCancelBookingDto emailCancelBookingDto){
        return emailService.sendEmailCancleBooking(emailCancelBookingDto);
    }

    @RequestMapping (value="/sendEmailCancelBookingForLessor",method =RequestMethod.POST)
    public String sendEmailCancelBookingForLessor(@RequestBody EmailCancelBookingForLessorDto emailCancelBookingForLessorDto){
        return emailService.sendEmailCancleBookingForLessor(emailCancelBookingForLessorDto);
    }

    @RequestMapping (value="/sendEmailRejectBooking",method =RequestMethod.POST)
    public String sendEmailRejectBooking(@RequestBody EmailCancelBookingDto emailCancelBookingDto){
        return emailService.sendEmailRejectBooking(emailCancelBookingDto);
    }

    @RequestMapping (value="/sendEmailRejectBookingForLessor",method =RequestMethod.POST)
    public String sendEmailRejectBookingForLessor(@RequestBody EmailCancelBookingForLessorDto emailCancelBookingForLessorDto){
        return emailService.sendEmailRejectBookingForLessor(emailCancelBookingForLessorDto);
    }

    @RequestMapping (value="/sendEmailApproveBooking",method =RequestMethod.POST)
    public String sendEmailApproveBooking(@RequestBody EmailSuccessBookingDto emailSuccessBookingDto){
        return emailService.sendEmailApproveBooking(emailSuccessBookingDto);
    }

    @RequestMapping (value="/sendEmailApproveBookingForLessor",method =RequestMethod.POST)
    public String sendEmailApproveBookingForLessor(@RequestBody EmailSuccessBookingForLessorDto emailSuccessBookingForLessorDto){
        return emailService.sendEmailApproveBookingForLessor(emailSuccessBookingForLessorDto);
    }

    @RequestMapping (value="/sendEmailRentingBooking",method =RequestMethod.POST)
    public String sendEmailRentingBooking(@RequestBody EmailSuccessBookingDto emailSuccessBookingDto){
        return emailService.sendEmailRentingBooking(emailSuccessBookingDto);
    }

    @RequestMapping (value="/sendEmailRentingBookingForLessor",method =RequestMethod.POST)
    public String sendEmailRentingBookingForLessor(@RequestBody EmailSuccessBookingForLessorDto emailSuccessBookingForLessorDto){
        return emailService.sendEmailRentingBookingForLessor(emailSuccessBookingForLessorDto);
    }

    @RequestMapping (value="/sendEmailDepositMadeBooking",method =RequestMethod.POST)
    public String sendEmailDepositMadeBooking(@RequestBody EmailSuccessBookingDto emailSuccessBookingDto){
        return emailService.sendEmailDepositMadeBooking(emailSuccessBookingDto);
    }

    @RequestMapping (value="/sendEmailDepositMadeBookingForLessor",method =RequestMethod.POST)
    public String sendEmailDepositMadeBookingForLessor(@RequestBody EmailSuccessBookingForLessorDto emailSuccessBookingForLessorDto){
        return emailService.sendEmailDepositMadeBookingForLessor(emailSuccessBookingForLessorDto);
    }

    @RequestMapping (value="/sendEmailDoneBooking",method =RequestMethod.POST)
    public String sendEmailDoneBooking(@RequestBody EmailSuccessBookingDto emailSuccessBookingDto){
        return emailService.sendEmailDoneBooking(emailSuccessBookingDto);
    }

    @RequestMapping (value="/sendEmailDoneBookingForLessor",method =RequestMethod.POST)
    public String sendEmailDoneBookingForLessor(@RequestBody EmailSuccessBookingForLessorDto emailSuccessBookingForLessorDto){
        return emailService.sendEmailDoneBookingForLessor(emailSuccessBookingForLessorDto);
    }

    @RequestMapping (value="/sendEmailDepositNotification",method =RequestMethod.POST)
    public String sendEmailDepositNotification(@RequestBody EmailSuccessBookingDto emailSuccessBookingDto){
        return emailService.sendEmailDepositNotification(emailSuccessBookingDto);
    }

    @RequestMapping (value="/changeDepositNotification/{id}",method =RequestMethod.POST)
    public boolean changeDepositNotification(@PathVariable Long id){
        return bookingService.changeDepositNotification(id);
    }

    @RequestMapping (value="/changeDepositCanceled/{id}",method =RequestMethod.POST)
    public boolean changeDepositCanceled(@PathVariable Long id){
        return bookingService.changeDepositCanceled(id);
    }

    @GetMapping("/motorbike/{motorbikeId}")
    public ResponseEntity<List<BookingRequest>> getBookingsByMotorbikeId(@PathVariable Long motorbikeId) {
        List<BookingRequest> bookings = bookingService.findByMotorbikeId(motorbikeId);
        return ResponseEntity.ok(bookings);
    }


    @GetMapping("/dates/motorbike/{motorbikeId}")
    public ResponseEntity<List<LocalDate>> getDatesByMotorbikeId(@PathVariable Long motorbikeId) {
        List<LocalDate> dates = bookingService.getDatesByMotorbikeId(motorbikeId);
        return ResponseEntity.ok(dates);
    }


    @RequestMapping (value="/saveDepositTime",method =RequestMethod.POST)
    public String saveDepositTime(@RequestBody DepositTimeDto depositTimeDto){
        return bookingService.saveDepositTime(depositTimeDto);
    }



}
