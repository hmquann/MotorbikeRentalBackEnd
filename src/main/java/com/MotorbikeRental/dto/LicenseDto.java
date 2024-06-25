package com.MotorbikeRental.dto;

import java.time.LocalDate;
import lombok.Data;
import com.MotorbikeRental.entity.User;
import org.springframework.web.multipart.MultipartFile;

@Data
public class LicenseDto {
    private String licenseNumber;

    private LocalDate birthOfDate;

    private MultipartFile licenseImageFile;
}
