package com.MotorbikeRental.service.impl;


import com.MotorbikeRental.dto.*;
import com.MotorbikeRental.entity.Model;
import com.MotorbikeRental.entity.Role;
import com.MotorbikeRental.exception.DuplicateUserException;
import com.MotorbikeRental.exception.InactiveUserException;
import com.MotorbikeRental.exception.InvalidCredentialsException;
import com.MotorbikeRental.repository.ModelRepository;
import com.MotorbikeRental.repository.RoleRepository;
import com.MotorbikeRental.repository.UserRepository;
import com.MotorbikeRental.service.AuthenticationService;
import com.MotorbikeRental.service.JWTService;
import com.MotorbikeRental.service.ModelService;
import lombok.RequiredArgsConstructor;
import com.MotorbikeRental.entity.User;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ModelServiceImpl implements ModelService {

    @Autowired
    private final ModelRepository modelRepository;
    @Override
    public List<ModelService> getModelByBrandId() {
        return null;
    }

    @Override
    public void addNewModel(RegisterMotorbikeDto registerMotorbikeDto) {

    }

    @Override
    public List<Model> getAllModel() {
        return modelRepository.findAll();
    }




//    @Override
//    public Model getModelIdByModelName(RegisterMotorbikeDto registerMotorbikeDto) {
//           return modelRepository.getModelByModelName(registerMotorbikeDto.getModelId());
//    }
}
