package com.MotorbikeRental.dto;

import com.MotorbikeRental.entity.FuelType;
import com.MotorbikeRental.entity.ModelType;
import lombok.Data;

@Data
public class ModelDto {
    private Long modelId;
    private String modelName;
    private int cylinderCapacity;
    private FuelType fuelType;
    private float fuelConsumption;
    private ModelType modelType;
    private BrandDto brand;
}
