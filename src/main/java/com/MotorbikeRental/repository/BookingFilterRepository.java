package com.MotorbikeRental.repository;

import com.MotorbikeRental.entity.Booking;
import com.MotorbikeRental.entity.BookingStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookingFilterRepository {

    private final EntityManager entityManager;

    public List<Booking> filterBookings(
            String tripType,
            Long userId,
            BookingStatus status,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String sort
    ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> criteriaQuery = criteriaBuilder.createQuery(Booking.class);
        Root<Booking> root = criteriaQuery.from(Booking.class);

        List<Predicate> predicates = new ArrayList<>();

        if (tripType != null && !tripType.isEmpty() && !tripType.equals("all")) {
            if (tripType.equals("renter")) {
                predicates.add(criteriaBuilder.equal(root.get("renter").get("id"), userId));
            } else if (tripType.equals("lessor")) {
                predicates.add(criteriaBuilder.equal(root.get("motorbike").get("user").get("id"), userId));
            }
        } else {
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("renter").get("id"), userId),
                    criteriaBuilder.equal(root.get("motorbike").get("user").get("id"), userId)
            ));
        }

        if (status != null && !status.equals("all")) {
            predicates.add(criteriaBuilder.equal(root.get("status"), status));
        }

        if (startTime != null && endTime != null) {
            predicates.add(
                    criteriaBuilder.and(
                            criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startTime),
                            criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endTime)
                    )
            );
        } else if(startTime != null && endTime == null){
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startTime)
            );
        } else if (startTime == null && endTime != null) {
            criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endTime);
        }

        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        // Add sorting
        if (sort != null && !sort.isEmpty()) {
            if (sort.equals("sortByEndDate")) {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("endDate")));
            } else if (sort.equals("sortByStartDate")) {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("startDate")));
            } else if (sort.equals("sortByBookingTimeDesc")) {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("bookingTime")));
            } else if (sort.equals("sortByBookingTimeAsc")) {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get("bookingTime")));
            }
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("bookingTime"))); // Default sort
        }

        TypedQuery<Booking> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
