package com.MotorbikeRental.service.impl;


import com.MotorbikeRental.dto.*;
import com.MotorbikeRental.entity.*;

import com.MotorbikeRental.dto.BookingRequest;
import com.MotorbikeRental.dto.FilterBookingDto;

import com.MotorbikeRental.repository.BookingFilterRepository;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
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
    private final UserService userService;;
    private final MotorbikeService motorbikeService;
    @Autowired
    private final MotorbikeRepository motorbikeRepository;

    private final BookingFilterRepository bookingFilterRepository;
    @Override
    public String saveBooking(BookingRequest bookingRequest) {
        Booking booking = new Booking();

        booking.setStartDate(bookingRequest.getStartDate());
        booking.setEndDate(bookingRequest.getEndDate());
        booking.setRenter(userService.getUserById(bookingRequest.getRenterId()));
        booking.setBookingTime(bookingRequest.getBookingTime());
        Motorbike motorbike=motorbikeRepository.findById(bookingRequest.getMotorbikeId())
                .orElseThrow(()->new NullPointerException("Not found"));
        booking.setMotorbike(motorbike);
        booking.setReceiveLocation(bookingRequest.getReceiveLocation());
        booking.setTotalPrice(bookingRequest.getTotalPrice());
        booking.setLongitude(bookingRequest.getLongitude());
        booking.setLatitude(bookingRequest.getLatitude());
        booking.setStatus(PENDING);
        booking.setDepositNoti(true);
        booking.setDepositCanceled(true);
        bookingRepository.save(booking);
        return "booking done";
    }


    @Override
    public String changeStatusBooking(Long id, String status) {
        Booking booking = bookingRepository.getById(id);
        BookingStatus newStatus = BookingStatus.fromString(status);

        booking.setStatus(newStatus);
        bookingRepository.save(booking);

        if (newStatus == BookingStatus.DONE) {
            Motorbike motorbike = booking.getMotorbike();
            motorbike.setTripCount(motorbike.getTripCount() + 1);
            motorbikeRepository.save(motorbike);
        }
        return "Change done";
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
        List<BookingDto> listBooks=getBookingListByMotorbikeId(motorbikeId);
        for(BookingDto booking:listBooks){
            if(booking.getStatus()==BookingStatus.BUSY&&booking.getStartDate().isAfter(startDate)&&
            booking.getEndTime().isBefore(endDate)){
            markAvailableDays(booking.getStartDate(),booking.getEndTime(),motorbikeId);
            }
        }
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

    @Override
    public void markAvailableDays(LocalDateTime startDate, LocalDateTime endDate, Long motorbikeId) {
       Booking bookings = bookingRepository.findBookingsByMotorbikeIdAndDateRange(motorbikeId, startDate, endDate);
        if (bookings != null) {
                bookingRepository.delete(bookings);
            }
       else {
            throw new IllegalArgumentException("No booking found for the given date range and motorbike ID.");
        }
    }

    public List<BookingRequest> getBookingListByRenterId(Long renterId) {
        List<Booking> bookingList= bookingRepository.getBookingListByRenterId(renterId);
        List<BookingRequest> bookingRequestList = bookingList.stream()
                .map(booking -> mapper.map(booking, BookingRequest.class))
                .collect(Collectors.toList());
        return bookingRequestList;
    }

    @Override
    public List<BookingRequest> getBookingListByLessorId(Long lessorId) {
        List<Booking> bookingList= bookingRepository.getBookingListByLessorId(lessorId);
        List<BookingRequest> bookingRequestList = bookingList.stream()
                .map(booking -> mapper.map(booking, BookingRequest.class))
                .collect(Collectors.toList());
        return bookingRequestList;
    }

    @Override
    public List<BookingRequest> filterBookings(FilterBookingDto filterBookingDto) {
        String tripType = filterBookingDto.getTripType();
        Long userId = filterBookingDto.getUserId();
        BookingStatus status = null;
        if(filterBookingDto.getStatus() != "all") {
            status = BookingStatus.fromString(filterBookingDto.getStatus());
        }
        String sort = filterBookingDto.getSort();
        LocalDateTime startTime = filterBookingDto.getStartTime();
        LocalDateTime endTime = filterBookingDto.getEndTime();
        List<Booking> bookingList= bookingFilterRepository.filterBookings(tripType,userId,status,startTime,endTime,sort);
        List<BookingRequest> bookingRequestList = bookingList.stream()
                .map(booking -> mapper.map(booking, BookingRequest.class))
                .collect(Collectors.toList());
        return bookingRequestList;
    }

    public boolean hasFeedbackBeenSent(Long bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId);
        return booking != null && booking.isFeedbackkk();
    }

    public BookingRequest convertToDto(Booking booking) {
        return mapper.map(booking, BookingRequest.class);
    }

    public List<BookingRequest> findByMotorbikeId(Long motorbikeId) {
        List<Booking> bookings = bookingRepository.findByMotorbikeId(motorbikeId);
        return bookings.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserToChat> getListUserFromBookingToChat(Long userId) {
        List<User> userList = bookingRepository.getListUserFromBookingToChat(userId);
        return userList.stream()
                .map(user -> mapper.map(user, UserToChat.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<LocalDate> getDatesByMotorbikeId(Long motorbikeId) {
        List<Booking> bookings = bookingRepository.findByMotorbikeId(motorbikeId);
        List<LocalDate> dates = new ArrayList<>();

        for (Booking booking : bookings) {
            if(booking.getStatus() == PENDING ||
                    booking.getStatus() == BookingStatus.PENDING_DEPOSIT ||
                        booking.getStatus() == BookingStatus.DEPOSIT_MADE ||
                            booking.getStatus() == BookingStatus.RENTING ||
                                booking.getStatus() == BookingStatus.BUSY){
                LocalDate start = booking.getStartDate().toLocalDate();
                LocalDate end = booking.getEndDate().toLocalDate();
                while (!start.isAfter(end)) {
                    dates.add(start);
                    start = start.plusDays(1);
                }
            }



        }
        return dates;
    }

    @Override
    public String saveDepositTime(DepositTimeDto depositTimeDto) {
        Booking booking = bookingRepository.findByBookingId(depositTimeDto.getBookingId());
        booking.setDepositTime(depositTimeDto.getDepositTime());
        bookingRepository.save(booking);
        return "save deposit time done";
    }

    @Override
    public boolean changeDepositNotification(Long bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId);
        booking.setDepositNoti(false);
        bookingRepository.save(booking);
        return false ;
    }

    @Override
    public boolean changeDepositCanceled(Long bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId);
        booking.setDepositCanceled(false);
        bookingRepository.save(booking);
        return false ;
    }




}
