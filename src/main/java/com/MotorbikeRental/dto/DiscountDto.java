package com.MotorbikeRental.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DiscountDto {

    private long id;

    private String name;

    private String code;

    private Long discount;

    private Date expirationDate;
}
