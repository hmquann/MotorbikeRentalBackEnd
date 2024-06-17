package com.MotorbikeRental.repository;

import com.MotorbikeRental.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Long> {

}
