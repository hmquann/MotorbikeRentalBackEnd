package com.MotorbikeRental.dto;

import com.MotorbikeRental.entity.VoucherType;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class DiscountDto {

    private String name;

    private String code;

    private String description;

    private VoucherType voucherType;

    private double discountPercent;

    private double maxDiscountMoney;

    private double discountMoney;

    private LocalDate startDate;

    private LocalDate expirationDate;

    private Integer quantity;

}
