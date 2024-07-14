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
    private Long bookingId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User renter;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private double totalPrice;
    @Column(columnDefinition = "nvarchar(255)")
    private String receiveLocation;

//    @Column(name = "payment_id")
//    private int paymentId;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    @ManyToOne
    @JoinColumn(name="motorbike_id")
    private Motorbike motorbike;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private FeedBack feedback;

}