package com.MotorbikeRental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDto {
    private Long id;
    private Long bookingId;
    private String feedbackContent;
    private int rate;
    private LocalDateTime feedbackTime;
    private String renterName;
    private Long renterId;

}
