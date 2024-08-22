package com.MotorbikeRental.dto;

import lombok.Data;

@Data
public class UpdateMotorbikeDto {
    private Long price;

    private Long overtimeFee;

    private Long overtimeLimit;

    private boolean delivery;

    private Long freeShipLimit;

    private Long deliveryFee;

    private String constraintMotorbike;

    private String motorbikeAddress;

    private Double longitude;

    private Double latitude;
}
