package com.MotorbikeRental.service;

import com.MotorbikeRental.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {
    Page<Transaction> getTransactionByUserId(Long userId, Pageable pageable);
    List<Transaction> getTransactionByUserIdd(Long userId);
}
