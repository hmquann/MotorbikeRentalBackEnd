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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id", nullable = false)
    private User renter;

    @Column(name = "motorbike_id", nullable = false)
    private int motorbikeId;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "insurance_id")
    private int insuranceId;

    @Column(name = "total_price", nullable = false)
    private float totalPrice;

    @Column(name = "receive_location", nullable = false)
    private String receiveLocation;

    @Column(name = "payment_id")
    private int paymentId;

    @Column(name = "status", length = 20, nullable = false)
    private String status;

    public Booking() {
    }

    public Booking(User renter, int motorbikeId, LocalDateTime startDate, LocalDateTime endDate, int insuranceId,
                   float totalPrice, String receiveLocation, int paymentId, String status) {
        this.renter = renter;
        this.motorbikeId = motorbikeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.insuranceId = insuranceId;
        this.totalPrice = totalPrice;
        this.receiveLocation = receiveLocation;
        this.paymentId = paymentId;
        this.status = status;
    }

    // Getters and setters

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public User getRenter() {
        return renter;
    }

    public void setRenter(User renter) {
        this.renter = renter;
    }

    public int getMotorbikeId() {
        return motorbikeId;
    }

    public void setMotorbikeId(int motorbikeId) {
        this.motorbikeId = motorbikeId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(int insuranceId) {
        this.insuranceId = insuranceId;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getReceiveLocation() {
        return receiveLocation;
    }

    public void setReceiveLocation(String receiveLocation) {
        this.receiveLocation = receiveLocation;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}