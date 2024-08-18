package com.MotorbikeRental.repository;

import com.MotorbikeRental.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MotorbikeFilterRepository {
    private final EntityManager entityManager;
    @Autowired
    private final BookingRepository bookingRepository;

    public List<Motorbike> listMotorbikeByFilter(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long brandId,
            ModelType modelType,
            Boolean isDelivery,
            Long minPrice,
            Long maxPrice) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Motorbike> criteriaQuery = criteriaBuilder.createQuery(Motorbike.class);
        Root<Motorbike> root = criteriaQuery.from(Motorbike.class);

        Join<Motorbike, Model> modelJoin = root.join("model", JoinType.LEFT);
        Join<Model, Brand> brandJoin = modelJoin.join("brand", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("status"), MotorbikeStatus.ACTIVE));

        if (brandId != null) {
            predicates.add(criteriaBuilder.equal(brandJoin.get("brandId"), brandId));
        }

        if (modelType != null) {
            predicates.add(criteriaBuilder.equal(modelJoin.get("modelType"), modelType));
        }

        if (isDelivery != null) {
            predicates.add(criteriaBuilder.equal(root.get("delivery"), isDelivery));
        }

        if (minPrice != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
        }

        if (maxPrice != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        if (startDate != null && endDate != null) {
            // Subquery to find Motorbike IDs that have bookings with startDate or endDate within the specified date range,
            // or the specified date range is within startDate and endDate, and status not REJECTED
            Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
            Root<Booking> bookingRoot = subquery.from(Booking.class);

            // Select Motorbike IDs
            subquery.select(bookingRoot.get("motorbike").get("id"));

            // Predicate for bookings that have startDate or endDate within the specified date range
            Predicate dateRangePredicate1 = criteriaBuilder.or(
                    criteriaBuilder.between(bookingRoot.get("startDate"), startDate, endDate),
                    criteriaBuilder.between(bookingRoot.get("endDate"), startDate, endDate)
            );

            // Predicate for bookings where the specified date range is within the booking's date range
            Predicate dateRangePredicate2 = criteriaBuilder.and(
                    criteriaBuilder.lessThanOrEqualTo(bookingRoot.get("startDate"), startDate),
                    criteriaBuilder.greaterThanOrEqualTo(bookingRoot.get("endDate"), endDate)
            );

            // Predicate for bookings that are not rejected
            Predicate statusNotRejectedPredicate = criteriaBuilder.notEqual(
                    bookingRoot.get("status"), BookingStatus.REJECTED
            );

            // Combine the predicates
            subquery.where(criteriaBuilder.and(
                    criteriaBuilder.or(dateRangePredicate1, dateRangePredicate2),
                    statusNotRejectedPredicate
            ));

            // Predicate to select motorbikes that are not in the subquery result
            Predicate noBookingDuringTimePredicate = criteriaBuilder.not(
                    root.get("id").in(subquery)
            );

            // Add the predicate to the main query
            criteriaQuery.where(noBookingDuringTimePredicate);
        }
        criteriaQuery.select(root).where(criteriaBuilder.and(predicates.toArray(new Predicate[0]))).distinct(true);

        TypedQuery<Motorbike> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }



    public List<Long> getFiveStarLessor() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();

        // Root for Feedback
        Root<FeedBack> feedbackRoot = criteriaQuery.from(FeedBack.class);

        // Join with Booking
        Join<FeedBack, Booking> bookingJoin = feedbackRoot.join("booking");

        // Join with Motorbike through Booking
        Join<Booking, Motorbike> motorbikeJoin = bookingJoin.join("motorbike");

        // Select lessorId and average rate
        criteriaQuery.multiselect(
                        motorbikeJoin.get("user").get("id").alias("lessorId"),
                        criteriaBuilder.avg(feedbackRoot.get("rate")).alias("avgRate")
                )
                .groupBy(motorbikeJoin.get("user").get("id"));

        // Execute query
        TypedQuery<Tuple> query = entityManager.createQuery(criteriaQuery);
        List<Tuple> result = query.getResultList();

        // Filter results where ceil(avgRate) = 5
        List<Long> lessorIds = result.stream()
                .filter(tuple -> Math.round((Double) tuple.get("avgRate")) == 5.0)
                .map(tuple -> (Long) tuple.get("lessorId"))
                .collect(Collectors.toList());
        return lessorIds;
    }



}