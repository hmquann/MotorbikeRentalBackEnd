package com.MotorbikeRental.controller;

import com.MotorbikeRental.entity.Schedule;
import com.MotorbikeRental.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/motorbike/{motorbikeId}")
    public List<Schedule> getSchedules(@PathVariable Long motorbikeId) {
        return scheduleService.getSchedulesByMotorbikeId(motorbikeId);
    }

    @PostMapping("/toggle")
    public Schedule toggleBusyDay(@RequestParam Long motorbikeId, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return scheduleService.toggleBusyDay(motorbikeId, localDate);
    }
}
