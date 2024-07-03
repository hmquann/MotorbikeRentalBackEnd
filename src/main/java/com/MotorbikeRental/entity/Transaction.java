package com.MotorbikeRental.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
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
    private Date transactionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User users;

}
