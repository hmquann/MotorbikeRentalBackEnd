package com.MotorbikeRental.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table (name = "discounts")
public class Discount {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

//    private Date startDate;

    private Date expirationDate;

//    private Long quantity;
//
//    private String description;

    private String code;

    private Long discount;




}
