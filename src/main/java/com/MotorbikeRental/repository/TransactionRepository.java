package com.MotorbikeRental.repository;

import com.MotorbikeRental.entity.Transaction;
import com.MotorbikeRental.entity.TransactionStatus;
import com.MotorbikeRental.entity.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByVnpTxnRef(String vnpTxnRef);

    Page<Transaction> findByUsers_Id (Long userId, Pageable pageable);

    List<Transaction> findByUsers_Id (Long userId);

    List<Transaction> findByStatus(TransactionStatus status);

    Page<Transaction> findByTypeAndStatus(TransactionType type, TransactionStatus status, Pageable pageable);
}
