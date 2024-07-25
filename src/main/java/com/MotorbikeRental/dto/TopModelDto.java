package com.MotorbikeRental.dto;

import lombok.Data;

@Data
public class TopModelDto {
        private String modelName;
        private Long bookingCount;
    public TopModelDto(String modelName, Long bookingCount) {
        this.modelName = modelName;
        this.bookingCount = bookingCount;
    }
}
