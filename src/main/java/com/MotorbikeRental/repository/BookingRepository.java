package com.MotorbikeRental.repository;

import com.MotorbikeRental.dto.BookingCountDto;
import com.MotorbikeRental.dto.BookingDto;
import com.MotorbikeRental.dto.MonthlyRevenueDto;
import com.MotorbikeRental.dto.TopModelDto;
import com.MotorbikeRental.entity.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findByMotorbikeId(Long motorbikeId);

    @Query("select b from Booking b where b.motorbike.id=:motorbikeId")
    List<Booking> getBookingByMotorbikeId(Long motorbikeId);

    Booking findByBookingId(Long bookingId);


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

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.motorbike.user.id = :userId AND b.status = 'DONE'")
    Long countBookingsByUserId(@Param("userId") Long userId);


    @Query("SELECT  new com.MotorbikeRental.dto.TopModelDto(m.modelName, COUNT(b.bookingId)) " +
            "FROM Booking b " +
            "JOIN b.motorbike mb " +
            "JOIN mb.model m " +
            "WHERE MONTH(b.endDate) = MONTH(CURRENT_DATE) " +
            "AND YEAR(b.endDate) = YEAR(CURRENT_DATE) " +
            "AND b.status='DONE' "+
            "GROUP BY m.id,m.modelName " +
            "ORDER BY COUNT(b.bookingId) DESC")
    List<TopModelDto>topModelsInCurrentMonth(Pageable pageable);
    @Query("SELECT SUM(b.totalPrice)"+
    "FROM  Booking  b join b.motorbike mb " +
            "WHERE MONTH(b.endDate) = MONTH(CURRENT_DATE) " +
            "AND YEAR(b.endDate) = YEAR(CURRENT_DATE) " +
            "AND b.status='DONE'"+
            "AND  mb.user.id=:lessorId")
    Long getAnnualInMonthByLessorId(Long lessorId);
    @Query("SELECT new com.MotorbikeRental.dto.MonthlyRevenueDto(MONTH(b.endDate), YEAR(b.endDate), SUM(b.totalPrice)) " +
            "FROM Booking b " +
            "WHERE b.status = 'DONE' " +
            "AND b.endDate >= :startDate " +
            "AND b.endDate <= :endDate " +
            "GROUP BY YEAR(b.endDate), MONTH(b.endDate) " +
            "ORDER BY YEAR(b.endDate), MONTH(b.endDate)")
    List<MonthlyRevenueDto> getRevenueForLastSixMonths(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    @Query("SELECT new com.MotorbikeRental.dto.BookingCountDto(MONTH(b.endDate), YEAR(b.endDate), COUNT(b)) " +
            "FROM Booking b " +
            "WHERE b.status = 'DONE' " +
            "AND b.endDate >= :startDate " +
            "AND b.endDate <= :endDate " +
            "GROUP BY YEAR(b.endDate), MONTH(b.endDate) " +
            "ORDER BY YEAR(b.endDate) DESC, MONTH(b.endDate) DESC")
    List<BookingCountDto> getBookingCountForLastTwoMonths(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    @Query("SELECT COUNT(b.bookingId) " +
            "FROM Booking b " +
            "WHERE b.receiveLocation LIKE %:address% " +
            "AND b.status='DONE'")
    Long countBookingByAddress(@Param("address") String address);
    @Query("SELECT COUNT(b.bookingId) " +
            "FROM Booking b " +
            "WHERE b.status = 'DONE'")
    Long countDoneBooking();

    @Query("SELECT DISTINCT u FROM User u " +
            "WHERE u.id IN (" +
            "   SELECT b.motorbike.user.id FROM Booking b WHERE b.renter.id = :userId" +
            ") OR u.id IN (" +
            "   SELECT b.renter.id FROM Booking b WHERE b.motorbike.user.id = :userId" +
            ") AND u.id != :userId")
    List<User> getListUserFromBookingToChat(@Param("userId") Long userId);
    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.motorbike.id = :motorbikeId " +
            "AND CAST(b.startDate AS DATE) <= CAST(:endDate AS DATE) " +
            "AND CAST(b.endDate AS DATE) >= CAST(:startDate AS DATE)")
    Booking findBookingsByMotorbikeIdAndDateRange(
            @Param("motorbikeId") Long motorbikeId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT b FROM Booking b")
    List<Booking> getAllBooking();
    @Query("SELECT b FROM Booking b WHERE b.motorbike.user.id = :lessorId")
    List<Booking> getAllBookingByLessorId(@Param("lessorId") Long lessorId);
}

