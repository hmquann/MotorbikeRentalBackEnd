package com.MotorbikeRental.service;

import com.MotorbikeRental.dto.*;
import com.MotorbikeRental.entity.Booking;
import com.MotorbikeRental.entity.Motorbike;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public interface BookingService {
    Booking saveBooking(BookingRequest bookingRequest);

    boolean changeStatusBooking();
    List<BookingDto> getBookingListByMotorbikeId(Long motorbikeId);
    List<TopModelDto> getTop5ModelsInCurrentMonth();
    Long getMonthlyRevenueByLessorId(Long lessorId);
    List<MonthlyRevenueDto> getSixMonthRevenue();
    List<BookingCountDto> getBookingCountForLastTwoMonths();
    Map<String,Long>mainLocationPercentage();
    void markBusyDays(LocalDateTime startDate, LocalDateTime endDate,Long motorbikeId);
}
