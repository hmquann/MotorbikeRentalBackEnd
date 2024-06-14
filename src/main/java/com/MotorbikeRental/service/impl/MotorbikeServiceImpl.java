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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class MotorbikeServiceImpl  implements MotorbikeService {
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
        return motorbikeRepository.save(motorbike);

    }

    @Override
    public void approveMotorbike(int motorbikeId) {

    }

    @Override
    public boolean checkExistPlate( String motorbikePlate) {
        return motorbikeRepository.findByMotorbikePlate(motorbikePlate).isEmpty();
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

}
