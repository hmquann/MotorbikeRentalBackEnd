package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.entity.MotorbikeImage;
import com.MotorbikeRental.repository.MotorbikeImageRepository;
import com.MotorbikeRental.service.MotorbikeImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MotorbikeImageServiceImpl implements MotorbikeImageService {
    @Autowired
    private MotorbikeImageRepository motorbikeImageRepository;
    @Override
    public void saveMotorbikeImage(List<String> motorbikeImages,Long id) {
        MotorbikeImage motorbikeImage = new MotorbikeImage();
        for(String imageUrl:motorbikeImages) {
            motorbikeImage.setId(id);
            motorbikeImage.setUrl(imageUrl);
            motorbikeImageRepository.save(motorbikeImage);
        }
    }
}
