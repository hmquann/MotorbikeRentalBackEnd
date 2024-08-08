package com.MotorbikeRental.service;

import com.MotorbikeRental.dto.LicenseDto;
import com.MotorbikeRental.dto.RegisterLicenseDto;
import com.MotorbikeRental.entity.License;
import com.MotorbikeRental.entity.LicenseStatus;
import org.springframework.data.domain.Page;

public interface LicenseService {
   void approveLicense(String licenseNumber);
   void rejectLicense(String licenseNumber);
   LicenseDto getLicenseByUserId(Long userId);
   void updateLicense(Long id,LicenseDto licenseDto);
   Page<LicenseDto> getPendingLicenseWithPagination(int page, int pageSize, LicenseStatus status);
}
