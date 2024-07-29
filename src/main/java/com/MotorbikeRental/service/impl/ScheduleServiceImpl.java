package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.entity.Motorbike;
import com.MotorbikeRental.entity.Schedule;
import com.MotorbikeRental.entity.ScheduleStatus;
import com.MotorbikeRental.repository.MotorbikeRepository;
import com.MotorbikeRental.repository.ScheduleRepository;
import com.MotorbikeRental.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MotorbikeRepository motorbikeRepository;
    @Override
    public List<Schedule> getSchedulesByMotorbikeId(Long motorbikeId) {
        return scheduleRepository.findByMotorbikeId(motorbikeId);
    }

    @Override
    public Schedule toggleBusyDay(Long motorbikeId, LocalDate date) {
        Optional<Motorbike> motorbike = motorbikeRepository.findById(motorbikeId);
        if (motorbike.isPresent()) {
            List<Schedule> schedules = scheduleRepository.findByMotorbikeId(motorbikeId);
            Optional<Schedule> existingSchedule = schedules.stream()
                    .filter(schedule -> schedule.getDate().equals(date))
                    .findFirst();

            if (existingSchedule.isPresent()) {
                Schedule schedule = existingSchedule.get();
                schedule.setStatus(schedule.getStatus() == ScheduleStatus.BUSY ? ScheduleStatus.AVAILABLE : ScheduleStatus.BUSY);
                return scheduleRepository.save(schedule);
            } else {
                Schedule newSchedule = new Schedule();
                newSchedule.setDate(date);
                newSchedule.setStatus(ScheduleStatus.BUSY);
                newSchedule.setMotorbike(motorbike.get());
                return scheduleRepository.save(newSchedule);
            }
        }
        throw new RuntimeException("Motorbike not found");
    }
}
