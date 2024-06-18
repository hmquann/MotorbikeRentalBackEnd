package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.entity.Motorbike;
import com.MotorbikeRental.entity.MotorbikeStatus;
import com.MotorbikeRental.repository.MotorbikeRepository;
import com.MotorbikeRental.service.MotorbikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MotorbikeServiceImpl  implements MotorbikeService {

    @Autowired
    private final MotorbikeRepository motorbikeRepository;


    @Override
    public List<Motorbike> getAllMotorbike() {
        return null;
    }

    @Override
    public List<Motorbike> getMotorbikeByLessorId() {
        return null;
    }

    @Override
    public List<Motorbike> getAllMotorbikeByStatus(List<Motorbike> motorbikeList, String status) {
        return null;
    }



    @Override
    public Motorbike registerMotorbike(Motorbike motorbike) {
//        if(motorbikeService.checkExistPlate(registerMotorbikeDto.getMotorbikePlate())){
//            throw  new ExistPlateException("The plate is exist in the system");
//        }
//        Motorbike motorbike=new Motorbike();
//        motorbike.setMotorbikePlate(registerMotorbikeDto.getMotorbikePlate());
//        motorbike.setConstraintMotorbike(registerMotorbikeDto.getConstraintMotorbike());
//        motorbike.setDelivery(registerMotorbikeDto.isDelivery());
//        motorbike.setDeliveryFee(registerMotorbikeDto.getDeliveyFeePerKilometer());
//        motorbike.setDistanceLimitPerDay(registerMotorbikeDto.getDistanceLimitPerDay());
//        motorbike.setModel(registerMotorbikeDto.getModelName());

        return motorbike;
    }

    @Override
    public void approveMotorbike(int motorbikeId) {

    }

    @Override
    public boolean checkExistPlate( String motorbikePlate) {
        return false;
    }

    @Override
    public void getMotorbikeByAddress( String address) {

    }

    @Override
    public void sortMotorbikeBycriteria( String criteria) {

    }

    @Override
    public List<Motorbike> getMotorbikeByBrand( String brandName) {
        return null;
    }

    @Override
    public List<Motorbike> getMotorbikeByModel( String modelName) {
        return null;
    }

    @Override
    public List<Motorbike> getMotorbikeByRequireTime(Date startDate, Date endDate) {
        return null;
    }

    @Override
    public List<Motorbike> getDeliveryMotorbike(boolean delivery) {
        return null;
    }

    @Override
    public List<Motorbike> getPendingMotorbikes() {
        return motorbikeRepository.findByStatus(MotorbikeStatus.PENDING);
    }

    @Override
    public Motorbike approveMotorbike(Long id) {
        return updateMotorbikeStatus(id, MotorbikeStatus.ACTIVE);
    }

    @Override
    public Motorbike rejectMotorbike(Long id) {
        return updateMotorbikeStatus(id, MotorbikeStatus.DEACTIVE);
    }

    private Motorbike updateMotorbikeStatus(Long id, MotorbikeStatus status) {
        Optional<Motorbike> motorbikeOpt = motorbikeRepository.findById(id);
        if (motorbikeOpt.isPresent()) {
            Motorbike motorbike = motorbikeOpt.get();
            motorbike.setStatus(status);
            return motorbikeRepository.save(motorbike);
        } else {
            throw new RuntimeException("Motorbike not found");
        }
    }


}
