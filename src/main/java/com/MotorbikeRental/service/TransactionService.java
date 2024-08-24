package com.MotorbikeRental.service;

import com.MotorbikeRental.dto.TransactionDto;
import com.MotorbikeRental.entity.Transaction;
import com.MotorbikeRental.entity.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {
    Page<Transaction> getTransactionByUserId(Long userId, int page, int size);
    List<Transaction> getTransactionByUserIdd(Long userId);
    Page<TransactionDto> getPendingWithdrawals(int page, int size);
    public Page<TransactionDto> getWithdrawals(TransactionStatus status, int page, int size);
}
