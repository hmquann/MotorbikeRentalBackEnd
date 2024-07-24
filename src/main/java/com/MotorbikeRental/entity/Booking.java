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

    private LocalDateTime bookingTime;

    private double totalPrice;
    @Column(columnDefinition = "nvarchar(255)")
    private String receiveLocation;

//    @Column(name = "payment_id")
//    private int paymentId;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @ManyToOne
    @JoinColumn(name="id")
    private Motorbike motorbike;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private FeedBack feedback;

    @Column(name = "is_feedback")
    private boolean isFeedbackkk;

}