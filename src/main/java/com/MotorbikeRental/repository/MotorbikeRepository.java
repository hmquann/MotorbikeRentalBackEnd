package com.MotorbikeRental.repository;


import com.MotorbikeRental.entity.Motorbike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MotorbikeRepository extends JpaRepository<Motorbike, Long>{
    @Override
    Optional<Motorbike> findById(Long id);
    boolean existsByMotorbikePlate(String motorbikePlate);
    Optional<Motorbike> findByMotorbikePlate(String motorbikePlate);

}
