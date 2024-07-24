package com.MotorbikeRental.dto;

import com.MotorbikeRental.entity.LicenseStatus;
import com.MotorbikeRental.entity.LicenseType;
import com.MotorbikeRental.entity.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.time.LocalDate;
@Data
public class LicenseDto {
    private String licenseNumber;
    private Long userId;
    private LocalDate birthOfDate;
    private String licenseImageUrl;
    private LicenseStatus status;
    private LicenseType licenseType;
}
