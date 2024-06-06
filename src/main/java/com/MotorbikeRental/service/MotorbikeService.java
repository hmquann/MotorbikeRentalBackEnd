package com.MotorbikeRental.service;

import com.MotorbikeRental.entity.Motorbike;

import java.util.Date;
import java.util.List;

public interface MotorbikeService {
    public List<Motorbike>getAllMotorbike();
    public List<Motorbike>getMotorbikeByLessorId();
    public List<Motorbike>getAllMotorbikeByStatus(List<Motorbike>motorbikeList,String status);
    public void registerMotorbike(Motorbike motorbike);
    public void approveMotorbike(int motorbikeId);
    public boolean checkExistPlate(List<Motorbike>motorbikeList,String motorbikePlate);
    public void getMotorbikeByAddress(List<Motorbike>motorbikeList,String address);
    public void sortMotorbikeBycriteria(List<Motorbike>motorbikeList,String criteria);
    public List<Motorbike> getMotorbikeByBrand(List<Motorbike>motorbikeList,String brandName);
    public List<Motorbike> getMotorbikeByModel(List<Motorbike>motorbikeList,String modelName);
    public List<Motorbike> getMotorbikeByRequireTime(Date startDate,Date endDate,List<Motorbike>motorbikeList);
    public List<Motorbike> getDeliveryMotorbike(boolean delivery);

}
