package com.MotorbikeRental.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ListActiveMotorbikeDto {
    private Long motorbikeId;

    private Long price;

    private boolean delivery;

    private Long modelId;

    private Long brandId;

}
