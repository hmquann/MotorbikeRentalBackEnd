package com.MotorbikeRental.dto;
import com.MotorbikeRental.entity.Brand;
import com.MotorbikeRental.entity.Model;
import com.MotorbikeRental.entity.MotorbikeImage;
import lombok.Data;

import java.util.List;

@Data
public class RegisterMotorbikeDto {
    private Brand brand;

    private Model model;

    private String motorbikePlate;

    private Long yearOfManufacture;

    //private String motorbikeImage;

    private Long price;

    private Long overtimeFee;

    private Long overtimeLimit;

    private boolean delivery;

    private Long freeshipDistance;

    private Long deliveyFeePerKilometer;

    private String constraintMotorbike;

    private String province;
    private String district;
    private String ward;
    private String addressDetail;

    private List<MotorbikeImage>motorbikeImages;
}
