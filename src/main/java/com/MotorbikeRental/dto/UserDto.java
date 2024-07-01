package com.MotorbikeRental.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private boolean gender;

    private BigDecimal balance;

    private boolean isActive;
}
