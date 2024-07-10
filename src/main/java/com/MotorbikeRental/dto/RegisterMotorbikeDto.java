package com.MotorbikeRental.dto;
import com.MotorbikeRental.entity.Brand;
import com.MotorbikeRental.entity.Model;
import com.MotorbikeRental.entity.MotorbikeImage;
import com.MotorbikeRental.entity.MotorbikeStatus;
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

    private List<MultipartFile> motorbikeImages;
  
    private MotorbikeStatus motorbikeStatus;

    private Long modelId;


}
