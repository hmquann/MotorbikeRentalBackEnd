package com.MotorbikeRental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Feedback")
@AllArgsConstructor
@RequiredArgsConstructor
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
