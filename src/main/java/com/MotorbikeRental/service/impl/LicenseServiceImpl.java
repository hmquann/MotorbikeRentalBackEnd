package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.dto.LicenseDto;
import com.MotorbikeRental.dto.RegisterLicenseDto;
import com.MotorbikeRental.entity.License;
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

        licenseRepository.approveLicense(licenseNumber);
    }

    @Override
    public LicenseDto getLicenseByUserId(Long userId) {
           License license= licenseRepository.getLicenseByuserId(userId);
           return mapper.map(license, LicenseDto.class);
    }

    @Override
    public Page<LicenseDto> getNotApproveLicenseWithPagination(int page, int pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize);
        List<License> licenseList=licenseRepository.getNotApproveLicenseList();
        List<LicenseDto> dtoList = licenseList.stream()
                .map(license -> mapper.map(license, LicenseDto.class))
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, dtoList.size());
    }
}
