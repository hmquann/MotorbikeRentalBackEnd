package com.MotorbikeRental.service;

import com.MotorbikeRental.dto.LicenseDto;
import com.MotorbikeRental.dto.RegisterLicenseDto;
import org.springframework.data.domain.Page;

public interface LicenseService {
   void approveLicense(String licenseNumber);
   LicenseDto getLicenseByUserId(Long userId);
   Page<LicenseDto> getNotApproveLicenseWithPagination(int page, int pageSize);
}
