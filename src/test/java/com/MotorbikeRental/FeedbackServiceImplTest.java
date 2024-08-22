package com.MotorbikeRental;

import com.MotorbikeRental.dto.FeedbackDto;
import com.MotorbikeRental.entity.Booking;
import com.MotorbikeRental.entity.BookingStatus;
import com.MotorbikeRental.entity.FeedBack;
import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.repository.BookingRepository;
import com.MotorbikeRental.repository.FeedbackRepository;
import com.MotorbikeRental.service.impl.FeedbackServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceImplTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    private ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        feedbackService = new FeedbackServiceImpl(feedbackRepository, bookingRepository, modelMapper);
    }

    @Test
    void testSendFeedback() {
        FeedbackDto feedbackDto = new FeedbackDto();
        feedbackDto.setBookingId(1L);
        feedbackDto.setRate(5);
        feedbackDto.setFeedbackContent("Good service");

        Booking booking = new Booking();
        booking.setBookingId(1L);
        booking.setStatus(BookingStatus.DONE);
        User renter = new User();
        renter.setId(1L);
        renter.setFirstName("John");
        renter.setLastName("Doe");
        booking.setRenter(renter);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(feedbackRepository.existsByBookingId(1L)).thenReturn(false);
        when(feedbackRepository.save(any(FeedBack.class))).thenAnswer(i -> i.getArguments()[0]);


        FeedbackDto result = feedbackService.sendFeedback(feedbackDto);


        assertNotNull(result);
        assertEquals(1L, result.getBookingId());
        assertEquals(5, result.getRate());
        assertEquals("Good service", result.getFeedbackContent());
        assertEquals("John Doe", result.getRenterName());
        assertNotNull(result.getFeedbackTime());

        verify(feedbackRepository, times(1)).save(any(FeedBack.class));
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void testSendFeedback_BookingNotFound() {
        FeedbackDto feedbackDto = new FeedbackDto();
        feedbackDto.setBookingId(1L);

        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());


        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            feedbackService.sendFeedback(feedbackDto);
        });

        assertEquals("Booking not found", exception.getMessage());
    }

    @Test
    void testSendFeedback_FeedbackAlreadyExists() {

        FeedbackDto feedbackDto = new FeedbackDto();
        feedbackDto.setBookingId(1L);

        Booking booking = new Booking();
        booking.setBookingId(1L);
        booking.setStatus(BookingStatus.DONE);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(feedbackRepository.existsByBookingId(1L)).thenReturn(true);


        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            feedbackService.sendFeedback(feedbackDto);
        });

        assertEquals("Feedback for this booking already exists", exception.getMessage());
    }

    @Test
    void testDeleteFeedbackById() {

        FeedBack feedback = new FeedBack();
        feedback.setId(1L);

        Booking booking = new Booking();
        booking.setBookingId(1L);
        feedback.setBooking(booking);

        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));


        boolean result = feedbackService.deleteFeedbackById(1L);


        assertTrue(result);
        verify(feedbackRepository, times(1)).delete(feedback);
    }

    @Test
    void testEditFeedback() {

        Long feedbackId = 1L;
        FeedbackDto feedbackDto = new FeedbackDto();
        feedbackDto.setRate(4);
        feedbackDto.setFeedbackContent("Updated content");

        FeedBack existingFeedback = new FeedBack();
        existingFeedback.setId(feedbackId);
        existingFeedback.setRate(5);
        existingFeedback.setFeedbackContent("Old content");
        existingFeedback.setFeedbackTime(LocalDateTime.now());

        Booking booking = new Booking();
        booking.setBookingId(1L);
        User renter = new User();
        renter.setId(1L);
        renter.setFirstName("John");
        renter.setLastName("Doe");
        booking.setRenter(renter);
        existingFeedback.setBooking(booking);

        when(feedbackRepository.findById(feedbackId)).thenReturn(Optional.of(existingFeedback));
        when(feedbackRepository.save(any(FeedBack.class))).thenReturn(existingFeedback);


        FeedbackDto result = feedbackService.editFeedback(feedbackId, feedbackDto);


        assertNotNull(result);
        assertEquals(feedbackId, result.getId());
        assertEquals(4, result.getRate());
        assertEquals("Updated content", result.getFeedbackContent());
        assertEquals("John Doe", result.getRenterName());

        verify(feedbackRepository, times(1)).save(existingFeedback);
    }
}
