package com.MotorbikeRental.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    private String vnpTxnRef;

    private String vnpResponseCode;

    private boolean isProcessed;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User users;

    @Column(columnDefinition = "nvarchar(255)")
    private String description;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "bank_name", nullable = false,columnDefinition = "nvarchar(255)")
    private String bankName;

    @Override
    public String toString() {
        return "Transaction{id=" + id + ", amount=" + amount + ", transactionDate=" + transactionDate + ", status=" + status + "}";
    }


}
