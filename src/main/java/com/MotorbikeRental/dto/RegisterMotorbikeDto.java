package com.MotorbikeRental.dto;
import com.MotorbikeRental.entity.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RegisterMotorbikeDto {


    private Long id;

    private Long userId;

    private String motorbikePlate;

    private Long yearOfManufacture;

    private Long price;

    private Long overtimeFee;

    private Long overtimeLimit;

    private boolean delivery;

    private Long freeShipLimit;

    private Long deliveryFee;

    private String constraintMotorbike;

    private String motorbikeAddress;

    private LicenseType licenseType;

    private List<MultipartFile> motorbikeImages;
  
    private MotorbikeStatus motorbikeStatus;

    private Long modelId;


}
