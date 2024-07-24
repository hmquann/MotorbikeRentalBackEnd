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
    @Column(name = "id")
    private Long id;

    @Column(name="price_per_day")
    private Long price;

    @Column(name="over_time_fee")
    private Long overtimeFee;

    @Column(name="over_time_limit")
    private Long overtimeLimit;


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
    @Column(columnDefinition = "nvarchar(255)")
    private String motorbikeAddress;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;
    @OneToMany(mappedBy = "motorbike")
    private List<MotorbikeImage>motorbikeImages;

    @Enumerated(EnumType.STRING)
    private LicenseType licenseType;

    @ManyToOne
    @JoinColumn(name="model_id")
    private Model model;


    @OneToMany(mappedBy = "motorbike")
    @JsonBackReference
    private List<Booking> bookingList;
}
