package com.MotorbikeRental.repository;

import com.MotorbikeRental.entity.Motorbike;
import com.MotorbikeRental.entity.MotorbikeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotorbikeImageRepository extends JpaRepository<MotorbikeImage, Long>{

}
