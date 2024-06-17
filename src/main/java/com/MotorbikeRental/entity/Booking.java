package com.MotorbikeRental.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingId;

    private Long renter_id;


    private Long motorbikeId;


    private LocalDateTime startDate;


    private LocalDateTime endDate;



    private double totalPrice;


    private String receiveLocation;

//    @Column(name = "payment_id")
//    private int paymentId;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;



}