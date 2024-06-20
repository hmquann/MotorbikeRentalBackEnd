package com.MotorbikeRental.repository;


import com.MotorbikeRental.entity.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
    boolean existsByModelName(String modelName);
    @Query("SELECT m FROM Model m WHERE m.modelName LIKE %:searchTerm% OR m.brand.brandName LIKE %:searchTerm%")
    Page<Model> searchByModelNameOrBrandName(String searchTerm, Pageable pageable);

}
