package com.MotorbikeRental.repository;

import com.MotorbikeRental.dto.BookingCountDto;
import com.MotorbikeRental.dto.MonthlyRevenueDto;
import com.MotorbikeRental.dto.TopModelDto;
import com.MotorbikeRental.entity.Booking;
import com.MotorbikeRental.entity.Model;
import com.MotorbikeRental.entity.Motorbike;
import com.MotorbikeRental.entity.BookingStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    @Query("select b from Booking b where b.motorbike.id=:motorbikeId")
    List<Booking> getBookingByMotorbikeId(Long motorbikeId);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.motorbike.user.id = :userId")
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

}
