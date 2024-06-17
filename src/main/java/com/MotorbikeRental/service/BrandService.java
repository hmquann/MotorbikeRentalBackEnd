package com.MotorbikeRental.service;

import com.MotorbikeRental.entity.Brand;

import org.springframework.data.domain.Page;


import java.util.List;

public interface BrandService {
    public List<Brand> getAllBrand();
    Brand createNewBrand(Brand brand);
    void deleteBrand(Long id);
    Brand updateBrand(Long id, Brand brand);
    Brand getBrand(Long id);
    Page<Brand> getBrandWithPagination(int page, int pageSize);

}
