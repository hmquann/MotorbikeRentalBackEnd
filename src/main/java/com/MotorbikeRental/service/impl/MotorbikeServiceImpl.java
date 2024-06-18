
package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.dto.RegisterMotorbikeDto;
import com.MotorbikeRental.entity.Model;
import com.MotorbikeRental.entity.Motorbike;
import com.MotorbikeRental.entity.MotorbikeStatus;

import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.exception.ExistPlateException;
import com.MotorbikeRental.repository.MotorbikeRepository;
import com.MotorbikeRental.repository.UserRepository;

import com.MotorbikeRental.service.MotorbikeService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MotorbikeServiceImpl  implements MotorbikeService {


    @Autowired
    private final MotorbikeRepository motorbikeRepository;


    private final ModelServiceImpl modelService;
    private final UserRepository userRepository;


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
        if(motorbikeRepository.existsByMotorbikePlate(motorbike.getMotorbikePlate())){
            throw  new ExistPlateException("The plate is exist in the system");
        }



//        Motorbike motorbike=new Motorbike();
//        motorbike.setMotorbikePlate(registerMotorbikeDto.getMotorbikePlate());
//        motorbike.setConstraintMotorbike(registerMotorbikeDto.getConstraintMotorbike());
//        motorbike.setDelivery(registerMotorbikeDto.isDelivery());
//        motorbike.setDeliveryFee(registerMotorbikeDto.getDeliveyFeePerKilometer());
//        motorbike.setDistanceLimitPerDay(registerMotorbikeDto.getDistanceLimitPerDay());


//        motorbike.setOutLimitFee(registerMotorbikeDto.getOutLimitFee());
//        motorbike.setOverTimeFee(registerMotorbikeDto.getOvertimeFee());
//        motorbike.setOverTimeLimit(registerMotorbikeDto.getOvertimeLimit());
//        motorbike.setPrice(registerMotorbikeDto.getPrice());
//           motorbike.setUser(.findByEmail("kiencbn2017@gmail.com"));
//        motorbike.setTripCount(Long.valueOf(0));
//        motorbike.setYearOfManuFacture(registerMotorbikeDto.getManufactureYear());
//        motorbike.setModel(registerMotorbikeDto.getModel());
//        motorbike.setStatus(MotorbikeStatus.PENDING);
//        motorbike.setFeatures(registerMotorbikeDto.getFeatureList());
        motorbike.setStatus(MotorbikeStatus.PENDING);
        motorbike.setTripCount(Long.valueOf(0));
        return motorbikeRepository.save(motorbike);


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
