package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.dto.BookingRequest;
import com.MotorbikeRental.dto.FilterBookingDto;
import com.MotorbikeRental.entity.Booking;
import com.MotorbikeRental.entity.BookingStatus;
import com.MotorbikeRental.entity.License;
import com.MotorbikeRental.entity.Motorbike;
import com.MotorbikeRental.repository.BookingFilterRepository;
import com.MotorbikeRental.repository.BookingRepository;
import com.MotorbikeRental.repository.MotorbikeRepository;
import com.MotorbikeRental.service.BookingService;
import com.MotorbikeRental.service.MotorbikeService;
import com.MotorbikeRental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final MotorbikeRepository motorbikeRepository;
    @Autowired
    private ModelMapper mapper;

    private final BookingFilterRepository bookingFilterRepository;
    @Override
    public Booking saveBooking(BookingRequest bookingRequest) {
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
        booking.setStatus(PENDING);

        return bookingRepository.save(booking);
    }

    @Override
    public Booking changeStatusBooking(Long id, String status) {
        Booking booking = bookingRepository.getById(id);
        booking.setStatus(BookingStatus.fromString(status));
        return bookingRepository.save(booking);
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
}
