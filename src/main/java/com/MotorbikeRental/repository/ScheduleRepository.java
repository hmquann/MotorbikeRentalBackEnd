package com.MotorbikeRental.repository;

import com.MotorbikeRental.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByMotorbikeId(Long motorbikeId);
}
