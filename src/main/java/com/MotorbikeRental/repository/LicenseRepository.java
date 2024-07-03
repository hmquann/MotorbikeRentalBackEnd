package com.MotorbikeRental.repository;

import com.MotorbikeRental.entity.License;
import com.MotorbikeRental.entity.LicenseStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LicenseRepository extends JpaRepository<License,String> {
    @Override
    Optional<License> findById(String s);
    @Query("Select l from License  l where l.user.id=:userId")
    License getLicenseByuserId(Long userId);
    @Query("Select l from License l where l.status = :status")
    List<License> getPendingLicenseList(@Param("status") LicenseStatus status);
    @Modifying
    @Transactional
    @Query("update License l set l.status =:status where l.licenseNumber = :licenseNumber")
    void changeLicense(@Param("licenseNumber") String licenseNumber, @Param("status") LicenseStatus status);

}
