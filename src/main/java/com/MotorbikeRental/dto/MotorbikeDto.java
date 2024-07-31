package com.MotorbikeRental.dto;

import com.MotorbikeRental.entity.MotorbikeStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class MotorbikeDto {
    private Long id;

    private Long userId;

    private String motorbikePlate;

    private Long yearOfManufacture;

    private Long price;

    private Long overtimeFee;

    private Long overtimeLimit;

    private boolean delivery;

    private Long freeShipLimit;

    private Long deliveryFee;

    private Long tripCount;

    private String constraintMotorbike;

    private String motorbikeAddress;

    private List<MotorbikeImageDto>motorbikeImages;

    private MotorbikeStatus motorbikeStatus;

    private Double longitude;

    private Double latitude;

    private ModelDto model;

    @JsonIgnoreProperties("motorbikes")
    private UserDto user;
}
