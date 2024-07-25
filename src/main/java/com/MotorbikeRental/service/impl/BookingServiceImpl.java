package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.dto.*;
import com.MotorbikeRental.entity.Booking;
import com.MotorbikeRental.entity.BookingStatus;
import com.MotorbikeRental.entity.Model;
import com.MotorbikeRental.entity.Motorbike;
import com.MotorbikeRental.repository.BookingRepository;
import com.MotorbikeRental.repository.MotorbikeRepository;
import com.MotorbikeRental.service.BookingService;
import com.MotorbikeRental.service.MotorbikeService;
import com.MotorbikeRental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.util.List;

import static com.MotorbikeRental.entity.BookingStatus.PENDING;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    @Autowired
    private final ModelMapper mapper;
    private final BookingRepository bookingRepository;
    @Autowired
    private final UserService userService;
    @Autowired
    private final MotorbikeRepository motorbikeRepository;
    @Override
    public Booking saveBooking(BookingRequest bookingRequest) {
        Booking booking = new Booking();
        booking.setStartDate(bookingRequest.getStartDate());
        booking.setEndDate(bookingRequest.getEndDate());
        booking.setRenter(userService.getUserById(bookingRequest.getRenterId()));
        Motorbike motorbike=motorbikeRepository.findById(bookingRequest.getMotorbikeId())
                .orElseThrow(()->new NullPointerException("Not found"));
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
    public List<BookingDto> getBookingListByMotorbikeId(Long motorbikeId) {
        List<Booking>bookings=bookingRepository.getBookingByMotorbikeId(motorbikeId);
        return bookings.stream()
                .map(this::convertToBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TopModelDto> getTop5ModelsInCurrentMonth() {
        return bookingRepository.topModelsInCurrentMonth(PageRequest.of(0,5));
    }

    @Override
    public Long getMonthlyRevenueByLessorId(Long lessorId) {
        return bookingRepository.getAnnualInMonthByLessorId(lessorId);
    }

    public BookingDto convertToBookingDto(Booking booking) {
        return mapper.map(booking, BookingDto.class);
    }
    @Override
    public List<MonthlyRevenueDto> getSixMonthRevenue(){
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = LocalDate.now()
                .minusMonths(5)
                .with(TemporalAdjusters.firstDayOfMonth())
                .atStartOfDay();
        return bookingRepository.getRevenueForLastSixMonths(startDate, endDate);
    }
    @Override
    public List<BookingCountDto> getBookingCountForLastTwoMonths() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate =LocalDate.now()
                .minusMonths(1)
                .with(TemporalAdjusters.firstDayOfMonth())
                .atStartOfDay();;
        return bookingRepository.getBookingCountForLastTwoMonths(startDate, endDate);
    }

    @Override
    public Map<String, Long> mainLocationPercentage() {
        Map<String,Long>mainLocationCount=new HashMap<>();
        mainLocationCount.put("Hà Nội",bookingRepository.countBookingByAddress("Hà Nội"));
        mainLocationCount.put("Hải Phòng",bookingRepository.countBookingByAddress("Hải Phòng"));
        mainLocationCount.put(("Đà Nẵng"),bookingRepository.countBookingByAddress("Đà Nẵng"));
        mainLocationCount.put(("Hồ Chí Minh"),bookingRepository.countBookingByAddress("thành phố Hồ Chí Minh"));
        mainLocationCount.put("Khác",bookingRepository.countDoneBooking()-bookingRepository.countBookingByAddress("Hà Nội")
        -bookingRepository.countBookingByAddress("Hải Phòng")-bookingRepository.countBookingByAddress("Đà Nẵng")-
                bookingRepository.countBookingByAddress("thành phố Hồ Chí Minh"));
        return mainLocationCount;
    }

    @Override
    public void markBusyDays(LocalDateTime startDate, LocalDateTime endDate,Long motorbikeId) {
        Booking b= new Booking();
        b.setStartDate(startDate);
        b.setEndDate(endDate);
        b.setMotorbike(motorbikeRepository.findById(motorbikeId).orElseThrow());
        b.setStatus(BookingStatus.BUSY);
        b.setRenter(b.getMotorbike().getUser());
        b.setTotalPrice(0);
        b.setReceiveLocation("");
        bookingRepository.save(b);
    }

}
