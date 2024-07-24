package com.MotorbikeRental.controller;


import com.MotorbikeRental.dto.DiscountDtoResponse;
import com.MotorbikeRental.dto.FeedbackDto;
import com.MotorbikeRental.entity.Booking;
import com.MotorbikeRental.entity.FeedBack;
import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.exception.ValidationException;
import com.MotorbikeRental.repository.BookingRepository;
import com.MotorbikeRental.repository.FeedbackRepository;
import com.MotorbikeRental.repository.UserRepository;
import com.MotorbikeRental.service.FeedbackService;
import com.MotorbikeRental.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    @Autowired
    private final FeedbackService feedbackService;

    @Autowired
    private final JWTService jwtService;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BookingRepository bookingRepository;

    @Autowired
    private final FeedbackRepository feedbackRepository;

    @PostMapping("/sendFeedback")
    public ResponseEntity<?> sendFeedback(@RequestHeader("Authorization") String accessToken,
                                                    @RequestBody FeedbackDto feedbackDto) {
        try {
            String token = accessToken.split(" ")[1];
            String username = jwtService.extractUsername(token);

            Optional<User> userOptional = userRepository.findByEmail(username);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            User user = userOptional.get();
            Booking booking = bookingRepository.findById(feedbackDto.getBookingId())
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

            if (!booking.getRenter().getId().equals(user.getId())) {
                throw new IllegalArgumentException("You are not allowed to send feedback for this booking");
            }

            FeedbackDto sendFeedback = feedbackService.sendFeedback(feedbackDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(sendFeedback);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }

    }

    @GetMapping("/{motorbikeId}/feedbacks")
    public ResponseEntity<List<FeedbackDto>> getFeedbacksForMotorbike(@PathVariable Long motorbikeId) {
        List<FeedbackDto> feedbacks = feedbackService.getFeedbacksByMotorbikeId(motorbikeId);
        return ResponseEntity.ok(feedbacks);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<FeedbackDto> deleteFeedback(@PathVariable Long id) {
        boolean isDeleted = feedbackService.deleteFeedbackById(id);
        if (!isDeleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateFeedback(@RequestHeader("Authorization") String accessToken,
                                            @PathVariable Long id,
                                            @RequestBody FeedbackDto feedbackDto) {
        try {

            String token = accessToken.split(" ")[1];
            String username = jwtService.extractUsername(token);


            Optional<User> userOptional = userRepository.findByEmail(username);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            User user = userOptional.get();


            FeedBack feedback = feedbackRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Feedback not found"));


            if (!feedback.getBooking().getRenter().getId().equals(user.getId())) {
                throw new IllegalArgumentException("You are not allowed to edit this feedback");
            }

            FeedbackDto updatedFeedback = feedbackService.editFeedback(id, feedbackDto);
            return ResponseEntity.ok(updatedFeedback);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid feedback ID or data: " + e.getMessage());
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: " + e.getMessage());
        }
    }
}
