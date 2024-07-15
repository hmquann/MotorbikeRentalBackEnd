package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.dto.FeedbackDto;
import com.MotorbikeRental.entity.Booking;
import com.MotorbikeRental.entity.BookingStatus;
import com.MotorbikeRental.entity.FeedBack;
import com.MotorbikeRental.repository.BookingRepository;
import com.MotorbikeRental.repository.FeedbackRepository;
import com.MotorbikeRental.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private final FeedbackRepository feedbackRepository;

    @Autowired
    private final BookingRepository bookingRepository;

    @Autowired
    private final ModelMapper modelMapper;


    @Override
    public FeedbackDto sendFeedback(FeedbackDto feedbackDto) {
        if (feedbackDto.getBookingId() == null) {
            throw new IllegalArgumentException("BookingId cannot be null");
        }
        Booking booking = bookingRepository.findById(feedbackDto.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));


        if (booking!= null &&  booking.getStatus() != BookingStatus.DONE) {
            throw new IllegalArgumentException("Feedback can only be created for bookings with status DONE");
        }

        if(feedbackRepository.existsByBookingId(feedbackDto.getBookingId())){
            throw new IllegalArgumentException("Feedback for this booking already exists");
        }

        FeedBack feedBack = modelMapper.map(feedbackDto, FeedBack.class);
        feedBack.setBooking(booking);
        feedBack.setFeedbackTime(LocalDateTime.now());
        feedBack = feedbackRepository.save(feedBack);

        feedbackDto.setId(feedBack.getId());
        feedbackDto.setFeedbackTime(feedBack.getFeedbackTime());
        return modelMapper.map(feedbackDto, FeedbackDto.class);

    }


    public List<FeedbackDto> getFeedbacksByMotorbikeId(Long motorbikeId) {
        List<FeedbackDto> feedbacks = feedbackRepository.findByMotorbikeId(motorbikeId);
        return feedbacks.stream()
                .map(feedback -> modelMapper.map(feedback, FeedbackDto.class))
                .collect(Collectors.toList());
    }
}
