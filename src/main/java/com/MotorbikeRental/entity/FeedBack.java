package com.MotorbikeRental.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Feedback")
public class FeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name="booking_id")
    private Booking booking;
    private String feedbackContent;
    private Long rate;



}
