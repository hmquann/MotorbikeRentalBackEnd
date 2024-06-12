package com.MotorbikeRental.repository;



import com.MotorbikeRental.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelRepository extends  JpaRepository<Model,Long>{
//   @Query("Select m from Model m where m.modelName=:modelName")
//    Model getModelByModelName(String modelName);
   @Query("Select m from Model m")
   public List<Model> getAllModel();
}
