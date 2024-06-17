package com.MotorbikeRental.repository;


import com.MotorbikeRental.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
    boolean existsByModelName(String modelName);

}
