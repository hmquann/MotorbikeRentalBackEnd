package com.MotorbikeRental.repository;

import com.MotorbikeRental.entity.Motorbike;
import com.MotorbikeRental.entity.MotorbikeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotorbikeImageRepository extends JpaRepository<MotorbikeImage, Long>{
    @Query("select m.url from MotorbikeImage m where m.motorbike.id=:id")
  public List<String> getMotorbikeImageByMotorbikeId(Long id);
}
