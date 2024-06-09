package com.MotorbikeRental.dto;

import com.MotorbikeRental.entity.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtAuthenticationResponse {

    private String token;

    private String refreshToken;

    private List<String> roles;

    private Long id;

    private double balance;

    private String firstName;

    private String lastName;



}
