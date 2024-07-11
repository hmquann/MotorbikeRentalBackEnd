package com.MotorbikeRental.dto;

import com.MotorbikeRental.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

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

    private Set<String> role;

    private String token;

    @JsonIgnoreProperties("user")
    private List<MotorbikeDto> motorbikes;
}
