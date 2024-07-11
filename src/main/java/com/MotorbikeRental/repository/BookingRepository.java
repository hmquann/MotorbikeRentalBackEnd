package com.MotorbikeRental.repository;

import com.MotorbikeRental.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    @Query("select b from Booking b where b.motorbike=:motorbike")
    ResponseEntity<List<Booking>> getBookingByMotorbikeId(Long motorbike);

    List<Booking> getBookingListByRenterId(Long renterId);
}
