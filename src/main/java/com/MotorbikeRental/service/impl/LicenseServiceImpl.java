package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.dto.LicenseDto;
import com.MotorbikeRental.dto.RegisterLicenseDto;
import com.MotorbikeRental.entity.License;
import com.MotorbikeRental.entity.LicenseStatus;
import com.MotorbikeRental.repository.LicenseRepository;
import com.MotorbikeRental.service.LicenseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LicenseServiceImpl implements LicenseService {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private LicenseRepository licenseRepository;
    @Override
    public void approveLicense(String licenseNumber) {
        licenseRepository.changeLicense(licenseNumber,LicenseStatus.APPROVED);
    }

    @Override
    public void rejectLicense(String licenseNumber) {
        licenseRepository.changeLicense(licenseNumber,LicenseStatus.REJECTED);
    }

    @Override
    public LicenseDto getLicenseByUserId(Long userId) {
           License license= licenseRepository.getLicenseByuserId(userId);
           return mapper.map(license, LicenseDto.class);
    }

    @Override
    public Page<LicenseDto> getPendingLicenseWithPagination(int page, int pageSize, LicenseStatus status) {

        Pageable pageable = PageRequest.of(page, pageSize);
        List<License> licenseList=licenseRepository.getPendingLicenseList(status);
        List<LicenseDto> dtoList = licenseList.stream()
                .map(license -> mapper.map(license, LicenseDto.class))
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, dtoList.size());
    }
}
