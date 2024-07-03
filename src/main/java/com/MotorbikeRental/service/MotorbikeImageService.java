package com.MotorbikeRental.service;

import com.MotorbikeRental.entity.MotorbikeImage;

import java.util.List;

public interface MotorbikeImageService {
    void saveMotorbikeImage(List<String> motorbikeImages, Long id);
}
