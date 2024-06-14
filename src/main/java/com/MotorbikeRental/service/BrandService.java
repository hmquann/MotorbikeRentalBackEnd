package com.MotorbikeRental.service;

import com.MotorbikeRental.entity.Brand;
import com.MotorbikeRental.repository.BrandRepository;

import java.util.List;

public interface BrandService {
    public List<Brand> getAllBrand();
    public void createNewBrand(String brandName);

}
