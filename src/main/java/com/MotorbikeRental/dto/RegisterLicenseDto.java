package com.MotorbikeRental.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RegisterLicenseDto {
    private String licenseNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthOfDate;

    private MultipartFile licenseImageFile;
}
