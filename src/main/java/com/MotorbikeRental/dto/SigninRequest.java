package com.MotorbikeRental.dto;

import lombok.Data;

@Data
public class SigninRequest {

    private String emailOrPhone;

    private String password;
}
