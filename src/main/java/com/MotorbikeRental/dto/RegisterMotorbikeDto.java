package com.MotorbikeRental.dto;
import com.MotorbikeRental.entity.Brand;
import com.MotorbikeRental.entity.Model;
import lombok.Data;

import java.awt.*;
import java.util.List;

@Data
public class RegisterMotorbikeDto {
    private Brand brand;

    private Model model;

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

    private List<Taskbar.Feature> featureList;

}
