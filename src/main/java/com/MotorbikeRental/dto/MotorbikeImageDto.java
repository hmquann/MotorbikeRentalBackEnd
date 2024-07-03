package com.MotorbikeRental.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class MotorbikeImageDto {

    private Long id;
    private Long motorbikeId;
    private String url;
}
