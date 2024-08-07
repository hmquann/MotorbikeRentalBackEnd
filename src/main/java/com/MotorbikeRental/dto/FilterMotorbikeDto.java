package com.MotorbikeRental.dto;

import com.MotorbikeRental.entity.FuelType;
import com.MotorbikeRental.entity.ModelType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class FilterMotorbikeDto implements Serializable {

    LocalDateTime startDate;

    LocalDateTime endDate;
    Double longitude;
    Double latitude;
    Boolean isFiveStar;
    Long brandId;
    FuelType fuelType;
    ModelType modelType;
    Boolean isDelivery;
    Long minPrice;
    Long maxPrice;


}
