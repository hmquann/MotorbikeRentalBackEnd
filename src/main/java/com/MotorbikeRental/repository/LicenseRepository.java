package com.MotorbikeRental.repository;

import com.MotorbikeRental.entity.License;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LicenseRepository extends JpaRepository<License,String> {
    @Override
    Optional<License> findById(String s);
    @Query("Select l from License  l where l.user.id=:userId")
    License getLicenseByuserId(Long userId);
    @Modifying
    @Transactional
    @Query("update License l set l.status = true where l.licenseNumber = :licenseNumber")
    void approveLicense(@Param("licenseNumber") String licenseNumber);

}
