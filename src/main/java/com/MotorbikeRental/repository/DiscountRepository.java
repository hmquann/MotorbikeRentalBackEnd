package com.MotorbikeRental.repository;


import com.MotorbikeRental.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    boolean existsByCode(String code);

    Discount findByCode(String code);


//    boolean deleteByCode(String code);



}
