package com.MotorbikeRental.controller;

import com.MotorbikeRental.dto.*;
import com.MotorbikeRental.entity.Booking;
import com.MotorbikeRental.entity.Transaction;
import com.MotorbikeRental.service.BookingService;
import com.MotorbikeRental.service.TransactionService;
import com.MotorbikeRental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    @Autowired
    private final BookingService bookingService;
    @Autowired
    private final TransactionService transactionService;
    @GetMapping("/topModels")
    public List<TopModelDto> getTop5Models(){
        return bookingService.getTop5ModelsInCurrentMonth();
    }
    @GetMapping("/getRevenue/{id}")
    public Long getRevenue(@PathVariable Long id){
        return  bookingService.getMonthlyRevenueByLessorId(id);
    }
    @GetMapping("/sixMonthRevenue")
    public List<MonthlyRevenueDto> getSixMonthRevenue() {
        return bookingService.getSixMonthRevenue();
    }
    @GetMapping("/twoRecentMonthBookingCount")
    public List<BookingCountDto>getTwoMonthBookingCount(){
        return  bookingService.getBookingCountForLastTwoMonths();
    }
    @GetMapping("/mainLocationCount")
    public Map<String,Long> getMainLocationCount(){
        return bookingService.mainLocationPercentage();
    }


    @GetMapping("/getAllBooking")
    public ResponseEntity<List<BookingRequest>> getAllBooking() {
        List<BookingRequest> bookingRequestList = bookingService.getAllBooking();
        return ResponseEntity.ok(bookingRequestList);
    }

    @GetMapping("/getAllTransactionByUserId/{id}")
    public ResponseEntity<List<Transaction>> getAllTransactionByUserId(@PathVariable Long id) {
        List<Transaction> transactionList = transactionService.getTransactionByUserIdd(id);
        return ResponseEntity.ok(transactionList);
    }


}
