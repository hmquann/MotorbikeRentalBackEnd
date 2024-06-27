package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.entity.License;
import com.MotorbikeRental.repository.LicenseRepository;
import com.MotorbikeRental.service.LicenseService;

import java.util.Optional;

public class LicenseServiceImpl implements LicenseService {
    private LicenseRepository licenseRepository;
    @Override
    public void approveLicense(String licenseNumber) {
         licenseRepository.approveLicense(licenseNumber);
    }

    @Override
    public Optional<License> getLicenseByUserId(Long userId) {
           return Optional.ofNullable(licenseRepository.getLicenseByuserId(userId));
    }
}
