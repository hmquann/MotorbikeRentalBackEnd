package com.MotorbikeRental.dto;

import lombok.Data;

import java.util.List;

@Data
public class MotorbikeDto {
    private Long id;

    private ModelDto model;

    private Long userId;

    private String motorbikePlate;

    private Long yearOfManufacture;


    private Long price;

    private Long overtimeFee;

    private Long overtimeLimit;

    private boolean delivery;

    private Long freeShipLimit;

    private Long deliveryFee;

    private String constraintMotorbike;

    private String motorbikeAddress;

    private List<String>motorbikeImages;
}
