package com.MotorbikeRental.config;
import com.MotorbikeRental.dto.ModelDto;
import com.MotorbikeRental.dto.UserDto;
import com.MotorbikeRental.entity.Model;
import com.MotorbikeRental.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

        @Bean
        public ModelMapper modelMapper() {
            // Tạo object và cấu hình
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration()
                    .setMatchingStrategy(MatchingStrategies.STANDARD);




            return modelMapper;
        }
    }