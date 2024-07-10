package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.entity.Motorbike;
import com.MotorbikeRental.entity.MotorbikeImage;
import com.MotorbikeRental.repository.MotorbikeImageRepository;
import com.MotorbikeRental.service.MotorbikeImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MotorbikeImageServiceImpl implements MotorbikeImageService {
    @Autowired
    private MotorbikeImageRepository motorbikeImageRepository;
    @Override
    public void saveMotorbikeImage(List<String> motorbikeImages,Long id) {
        for (String url : motorbikeImages) {
            MotorbikeImage motorbikeImage = new MotorbikeImage();
            motorbikeImage.setUrl(url);
            Motorbike motorbike = new Motorbike();
            motorbike.setId(id);
            motorbikeImage.setMotorbike(motorbike);
            motorbikeImageRepository.save(motorbikeImage);
        }
    }

    @Override
    public List<String> getMotorbikeImage(Long id) {
        return motorbikeImageRepository.getMotorbikeImageByMotorbikeId(id);
    }
}
