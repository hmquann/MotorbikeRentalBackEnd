package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.entity.Motorbike;
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
    public void registerMotorbike(Motorbike motorbike) {

    }

    @Override
    public void approveMotorbike(int motorbikeId) {

    }

    @Override
    public boolean checkExistPlate(List<Motorbike> motorbikeList, String motorbikePlate) {
        return false;
    }

    @Override
    public void getMotorbikeByAddress(List<Motorbike> motorbikeList, String address) {

    }

    @Override
    public void sortMotorbikeBycriteria(List<Motorbike> motorbikeList, String criteria) {

    }

    @Override
    public List<Motorbike> getMotorbikeByBrand(List<Motorbike> motorbikeList, String brandName) {
        return null;
    }

    @Override
    public List<Motorbike> getMotorbikeByModel(List<Motorbike> motorbikeList, String modelName) {
        return null;
    }

    @Override
    public List<Motorbike> getMotorbikeByRequireTime(Date startDate, Date endDate, List<Motorbike> motorbikeList) {
        return null;
    }

    @Override
    public List<Motorbike> getDeliveryMotorbike(boolean delivery) {
        return null;
    }




}
