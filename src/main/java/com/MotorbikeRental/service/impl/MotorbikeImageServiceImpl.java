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

        for(String imageUrl:motorbikeImages) {
            MotorbikeImage motorbikeImage = new MotorbikeImage();
            motorbikeImage.setUrl(imageUrl);
            motorbikeImageRepository.save(motorbikeImage);
        }
    }

    @Override
    public List<String> getMotorbikeImage(Long id) {
        return motorbikeImageRepository.getMotorbikeImageByMotorbikeId(id);
    }
}
