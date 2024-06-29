package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.entity.License;
import com.MotorbikeRental.repository.LicenseRepository;
import com.MotorbikeRental.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class LicenseServiceImpl implements LicenseService {
    @Autowired
    private LicenseRepository licenseRepository;
    @Override
    public void approveLicense(String licenseNumber) {

        licenseRepository.approveLicense(licenseNumber);
    }

    @Override
    public Optional<License> getLicenseByUserId(Long userId) {
           return Optional.ofNullable(licenseRepository.getLicenseByuserId(userId));
    }

    @Override
    public Page<License> getLicenseWithPagination(int page, int pageSize) {
        return licenseRepository.findAll(PageRequest.of(page,pageSize));
    }
}
