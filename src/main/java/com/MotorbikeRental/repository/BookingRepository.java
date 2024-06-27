package com.MotorbikeRental.repository;

import com.MotorbikeRental.entity.Booking;
import com.MotorbikeRental.entity.Motorbike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    @Query("select b from Booking b where b.motorbikeId=:motorbikeId")
    ResponseEntity<List<Booking>> getBookingByMotorbikeId(Long motorbikeId);
}
