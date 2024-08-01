package com.MotorbikeRental.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

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
    @Column(columnDefinition = "nvarchar(255)")
    private String feedbackContent;
    private int rate;
    private LocalDateTime feedbackTime;



}
