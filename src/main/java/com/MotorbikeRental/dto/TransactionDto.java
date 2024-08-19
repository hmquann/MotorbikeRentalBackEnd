package com.MotorbikeRental.dto;

import com.MotorbikeRental.entity.TransactionStatus;
import com.MotorbikeRental.entity.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private Long id;

    private BigDecimal amount;

    private String vnpTxnRef;

    private String vnpResponseCode;

    private boolean isProcessed;

    private TransactionStatus status;

    private TransactionType type;

    private LocalDateTime transactionDate;

    private String description;

    private String accountNumber;

    private String bankName;

    private UserDto user;

}
