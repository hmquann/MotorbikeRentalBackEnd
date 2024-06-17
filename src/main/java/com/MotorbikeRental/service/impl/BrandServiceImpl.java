package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.entity.Brand;
import com.MotorbikeRental.repository.BrandRepository;
import com.MotorbikeRental.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    @Autowired
    private final BrandRepository brandRepository;
    @Override
    public List<Brand> getAllBrand() {
        return brandRepository.findAll();
    }

    @Override
    public void createNewBrand(String brandName) {

    }
}
