package com.MotorbikeRental.service;

import com.MotorbikeRental.entity.License;

import java.util.Optional;

public interface LicenseService {
   void approveLicense(String licenseNumber);
   Optional<License> getLicenseByUserId(Long userId);

}
