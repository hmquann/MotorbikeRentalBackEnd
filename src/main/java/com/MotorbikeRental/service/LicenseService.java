package com.MotorbikeRental.service;

import com.MotorbikeRental.entity.Brand;
import com.MotorbikeRental.entity.License;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface LicenseService {
   void approveLicense(String licenseNumber);
   Optional<License> getLicenseByUserId(Long userId);
   Page<License> getLicenseWithPagination(int page, int pageSize);
}
