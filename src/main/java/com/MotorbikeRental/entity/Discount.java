package com.MotorbikeRental.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Table (name = "Discount")
public class Discount {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private VoucherType voucherType;

    private double discountPercent;

    private double maxDiscountMoney;

    private double discountMoney;

    private LocalDate startDate;

    private LocalDate expirationDate;

    private boolean expired;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "created_by_userId")
    private User createdBy;

    @ManyToMany(mappedBy = "discounts", cascade = CascadeType.REMOVE)
    private Set<User> users = new HashSet<>();

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, description, voucherType, discountPercent, maxDiscountMoney, discountMoney, startDate, expirationDate, expired, quantity);
    }


}
