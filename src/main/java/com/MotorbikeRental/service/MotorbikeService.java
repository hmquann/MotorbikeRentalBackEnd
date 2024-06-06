package com.MotorbikeRental.service;

import com.MotorbikeRental.dto.RegisterMotorbikeDto;
import com.MotorbikeRental.entity.Motorbike;

import java.util.Date;
import java.util.List;

public interface MotorbikeService {
    public List<Motorbike>getAllMotorbike();

    public List<Motorbike>getMotorbikeByLessorId();

    public List<Motorbike>getAllMotorbikeByStatus(List<Motorbike>motorbikeList,String status);

    public void registerMotorbike(RegisterMotorbikeDto registerMotorbikeDto);

    public void approveMotorbike(int motorbikeId);

    public boolean checkExistPlate(String motorbikePlate);

    public void getMotorbikeByAddress(String address);

    public void sortMotorbikeBycriteria(String criteria);

    public List<Motorbike> getMotorbikeByBrand(String brandName);

    public List<Motorbike> getMotorbikeByModel(String modelName);

    public List<Motorbike> getMotorbikeByRequireTime(Date startDate,Date endDate);

    public List<Motorbike> getDeliveryMotorbike(boolean delivery);

}