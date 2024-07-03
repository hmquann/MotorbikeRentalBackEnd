package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.entity.Transaction;
import com.MotorbikeRental.repository.TransactionRepository;
import com.MotorbikeRental.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TransactionSeriveImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Page<Transaction> getTransactionByUserId(Long userId, Pageable pageable) {
        return transactionRepository.findByUsers_Id(userId,pageable);
    }

    @Override
    public List<Transaction> getTransactionByUserIdd(Long userId) {
        return transactionRepository.findByUsers_Id(userId);
    }
}
