package com.MotorbikeRental.service;

import com.MotorbikeRental.dto.FeedbackDto;

import java.util.List;

public interface FeedbackService {
    FeedbackDto sendFeedback(FeedbackDto feedbackDto);

    List<FeedbackDto> getFeedbacksByMotorbikeId(Long motorbikeId);

    boolean deleteFeedbackById(Long id);

    FeedbackDto editFeedback(Long feedbackId, FeedbackDto feedbackDto);
}
