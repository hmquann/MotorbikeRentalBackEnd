package com.MotorbikeRental.controller;

import com.MotorbikeRental.dto.TransactionDto;
import com.MotorbikeRental.entity.Brand;
import com.MotorbikeRental.entity.Transaction;
import com.MotorbikeRental.entity.TransactionStatus;
import com.MotorbikeRental.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private final TransactionService transactionService;

    @GetMapping("/{userId}/{page}/{pageSize}")
    public ResponseEntity<Page<Transaction>> getTransactionsByUserId(@PathVariable Long userId,
                                                                     @PathVariable int page,
                                                                     @PathVariable int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return ResponseEntity.ok(transactionService.getTransactionByUserId(userId,pageable));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionsByUserIdd(@PathVariable Long userId){
        return ResponseEntity.ok(transactionService.getTransactionByUserIdd(userId));
    }

    @GetMapping("/withdrawalsRequest")
    public Page<TransactionDto> getPendingWithdrawals(
            @RequestParam int page,
            @RequestParam int size) {
        return transactionService.getPendingWithdrawals(page, size);
    }

    @GetMapping("/withdrawList")
    public Page<TransactionDto> getWithdrawals(
            @RequestParam TransactionStatus status,
            @RequestParam int page,
            @RequestParam int size) {
        return transactionService.getWithdrawals(status,page, size);
    }
}
