package com.MotorbikeRental.service;

import com.MotorbikeRental.entity.Schedule;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface ScheduleService {
    List<Schedule> getSchedulesByMotorbikeId(Long motorbikeId);
    Schedule toggleBusyDay(Long motorbikeId, LocalDate date);

}
