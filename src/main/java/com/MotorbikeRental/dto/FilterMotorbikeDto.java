package com.MotorbikeRental.dto;

import com.MotorbikeRental.entity.FuelType;
import com.MotorbikeRental.entity.ModelType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class FilterMotorbikeDto implements Serializable {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime endTime;
    String address;
    boolean isFiveStar;
    Long brandId;
    FuelType fuelType;
    ModelType modelType;
    boolean isDelivery;


}