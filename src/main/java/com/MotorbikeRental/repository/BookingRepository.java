package com.MotorbikeRental.repository;

import com.MotorbikeRental.entity.Booking;
import com.MotorbikeRental.entity.BookingStatus;
import com.MotorbikeRental.entity.Motorbike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    @Query("select b from Booking b where b.motorbike.id=:motorbikeId")
    ResponseEntity<List<Booking>> getBookingByMotorbikeId(Long motorbikeId);

    List<Booking> getBookingListByRenterId(Long renterId);

    @Query("SELECT b FROM Booking b WHERE b.motorbike.user.id = :lessorId")
    List<Booking> getBookingListByLessorId(@Param("lessorId") Long lessorId);

    @Query("SELECT b FROM Booking b " +
            "WHERE (:tripType = 'renter' AND b.renter.id = :userId) " +
            "   OR (:tripType = 'lessor' AND b.motorbike.user.id = :userId) " +
            "   OR (:tripType = 'all') " +
            "AND (:status = 'all' OR b.status = :status) " +
            "AND b.startDate >= :startTime " +
            "AND b.endDate <= :endTime " +
            "ORDER BY CASE WHEN :sort = 'sortByEndDate' THEN b.endDate " +
            "              WHEN :sort = 'sortByStartDate' THEN b.startDate END ASC")
    List<Booking> filterBookings(@Param("tripType") String tripType,
                                 @Param("userId") Long userId,
                                 @Param("status") BookingStatus status,
                                 @Param("sort") String sort,
                                 @Param("startTime") LocalDateTime startTime,
                                 @Param("endTime") LocalDateTime endTime);
}
