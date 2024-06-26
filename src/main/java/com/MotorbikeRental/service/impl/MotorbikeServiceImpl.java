
package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.dto.ListActiveMotorbikeDto;
import com.MotorbikeRental.dto.ModelDto;
import com.MotorbikeRental.dto.RegisterMotorbikeDto;
import com.MotorbikeRental.entity.*;

import com.MotorbikeRental.exception.ExistPlateException;
import com.MotorbikeRental.repository.ModelRepository;
import com.MotorbikeRental.repository.MotorbikeRepository;
import com.MotorbikeRental.repository.UserRepository;

import com.MotorbikeRental.service.MotorbikeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.mapper.Mapper;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final ModelMapper mapper;
    @Autowired
    private final MotorbikeRepository motorbikeRepository;

    private final ModelRepository modelRepository;


    private final ModelServiceImpl modelService;
    private final UserRepository userRepository;


    @Override
    public Page<RegisterMotorbikeDto> getAllMotorbike(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<Motorbike> motorbikeList = motorbikeRepository.findAll();
        List<RegisterMotorbikeDto> dtoList = motorbikeList.stream()
                .map(motorbike -> mapper.map(motorbike, RegisterMotorbikeDto.class))
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, dtoList.size());
    }

    @Override
    public Page<Motorbike> getMotorbikeWithPagination(int page, int pageSize){
        return motorbikeRepository.findAll(PageRequest.of(page,pageSize));
    }

    @Override
    public Page<Motorbike> searchByPlate(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Motorbike> motorbikePage = motorbikeRepository.searchByMotorbikePlate(searchTerm, pageable);
        return motorbikePage;
    }

    @Override
    public void toggleMotorbikeStatus(Long id) {
        Motorbike motorbike = motorbikeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Motorbike not found with id: " + id));

            if (motorbike.getStatus() == MotorbikeStatus.ACTIVE) {
                motorbike.setStatus(MotorbikeStatus.DEACTIVE);
            } else if (motorbike.getStatus() == MotorbikeStatus.DEACTIVE) {
                motorbike.setStatus(MotorbikeStatus.ACTIVE);
            }
            motorbikeRepository.save(motorbike);

        }



    @Override
    public List<Motorbike> getMotorbikeByLessorId() {
        return null;
    }

    @Override
    public Page<ListActiveMotorbikeDto> getAllMotorbikeByStatus(MotorbikeStatus status, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<Motorbike> motorbikeList = motorbikeRepository.getAllMotorbikeByStatus(status);
        List<ListActiveMotorbikeDto> dtoList = motorbikeList.stream()
                .map(motorbike -> mapper.map(motorbike, ListActiveMotorbikeDto.class))
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, dtoList.size());
    }
    @Override

    public Motorbike registerMotorbike(RegisterMotorbikeDto registerMotorbikeDto) {
        if(motorbikeRepository.existsByMotorbikePlate(registerMotorbikeDto.getMotorbikePlate())){
            throw  new ExistPlateException("The plate is exist in the system");
        }
        Motorbike motorbike=mapper.map(registerMotorbikeDto,Motorbike.class);
        motorbike.setStatus(MotorbikeStatus.PENDING);
        motorbike.setTripCount(Long.valueOf(0));
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


    @Override
    public Page<RegisterMotorbikeDto> getPendingMotorbikes(MotorbikeStatus status,int page,int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<Motorbike> motorbikeList = motorbikeRepository.getAllMotorbikeByStatus(status);
        List<RegisterMotorbikeDto> dtoList = motorbikeList.stream()
                .map(motorbike -> mapper.map(motorbike, RegisterMotorbikeDto.class))
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, dtoList.size());
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

