package com.MotorbikeRental.repository;

import com.MotorbikeRental.dto.FeedbackDto;
import com.MotorbikeRental.entity.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedBack, Long> {
    @Query("SELECT COUNT(f) > 0 FROM FeedBack f WHERE f.booking.bookingId = :bookingId")
    boolean existsByBookingId(Long bookingId);

    @Query("SELECT new com.MotorbikeRental.dto.FeedbackDto(f.id, f.booking.bookingId, f.feedbackContent, f.rate, f.feedbackTime,CONCAT(r.firstName, ' ' ,r.lastName)) FROM FeedBack f JOIN f.booking b JOIN b.motorbike m JOIN b.renter r WHERE m.id = :motorbikeId")
    List<FeedbackDto> findByMotorbikeId(@Param("motorbikeId") Long motorbikeId);
}
