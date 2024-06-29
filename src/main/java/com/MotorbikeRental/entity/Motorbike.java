package com.MotorbikeRental.entity;



import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.awt.*;
import java.util.*;
import java.util.List;
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
    @Enumerated(EnumType.STRING)
    private MotorbikeStatus status;

    @Column(name="constraint_motorbike")
    private String constraintMotorbike;

    @Column(name="year_of_manufacture")
    private Long yearOfManuFacture;
    @Column(name = "motorbike_plate",unique = true,length = 11)
    private String motorbikePlate;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    //thang ngu doc di
//    @JsonManagedReference
    private User user;

//    @JsonManagedReference
//    public User user(){
//        return user;
//    }

    @ManyToOne
    @JoinColumn(name="model_id")
//    @JsonManagedReference
    private Model model;


    @OneToMany(mappedBy = "motorbike")
    private List<Booking> bookingList;
}
