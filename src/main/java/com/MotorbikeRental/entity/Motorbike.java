package com.MotorbikeRental.entity;



import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "[Motorbike]")
public class Motorbike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="price_per_day")
    private Long price;

    @Column(name="over_time_fee")
    private Long overTimeFee;

    @Column(name="over_time_limit")
    private Long overTimeLimit;

    @Column(name="distance_limit_per_day")
    private Long distanceLimitPerDay;

    @Column(name="out_limit_fee")
    private Long outLimitFee;

    @Column(name="trip_count")
    private Long tripCount;

    @Column(name = "delivery")
    boolean delivery;

    @Column(name="free_ship_limit")
    private Long freeShipLimit;

    @Column(name="delivery_fee")
    private Long deliveryFee;

    @Column(name="status")
    private Enum<MotorbikeStatus> status;

    @Column(name="constraint_motorbike")
    private String constraintMotorbike;

    @Column(name="year_of_manufacture")
    private int yearOfManuFacture;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
