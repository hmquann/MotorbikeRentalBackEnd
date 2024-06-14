package com.MotorbikeRental.repository;

import com.MotorbikeRental.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    boolean existsByBrandName(String brandName);

    boolean existsByBrandId(Long id);
}
