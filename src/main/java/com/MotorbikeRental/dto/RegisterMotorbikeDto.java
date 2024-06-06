package com.MotorbikeRental.dto;
import lombok.Data;

@Data
public class RegisterMotorbikeDto {
    private String brandName;

    private String modelName;

    private String motorbikePlate;

    private Long manufactureYear;

    //private String motorbikeImage;

    private Long price;

    private Long overtimeFee;

    private Long overtimeLimit;

    private Long distanceLimitPerDay;

    private Long outLimitFee;

    private boolean delivery;

    private Long freeshipDistance;

    private Long deliveyFeePerKilometer;

    private String constraintMotorbike;



}
