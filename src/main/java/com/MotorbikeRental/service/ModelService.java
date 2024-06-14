package com.MotorbikeRental.service;

import com.MotorbikeRental.dto.RegisterMotorbikeDto;
import com.MotorbikeRental.entity.Model;

import java.util.List;

public interface ModelService {

    public List<ModelService> getModelByBrandId();

    public void addNewModel(RegisterMotorbikeDto registerMotorbikeDto);

    public List<Model> getAllModel();
   
}