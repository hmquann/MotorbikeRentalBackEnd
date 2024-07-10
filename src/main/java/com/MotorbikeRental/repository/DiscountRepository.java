package com.MotorbikeRental.repository;


import com.MotorbikeRental.entity.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    boolean existsByCode(String code);

    Discount findByCode(String code);

    @Query("SELECT d FROM Discount d JOIN d.createdBy.roles r WHERE r.name IN :roles AND d.createdBy.id = :userId")
    Page<Discount> findByCreatedBy(@Param("roles") List<String> roles, @Param("userId") Long userId, Pageable pageable);

    @Query("SELECT d FROM Discount d WHERE d.expired = false AND d.expirationDate < :today")
    List<Discount> findByExpiredFalseAndExpirationDateBefore(@Param("today") LocalDate today);

//    boolean deleteByCode(String code);



}
