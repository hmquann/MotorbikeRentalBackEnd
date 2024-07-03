package com.MotorbikeRental.config;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class CloudinaryConfig {


        @Bean
        public Cloudinary cloudinary() {
            return new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "dmjxutjbf",
                    "api_key", "272254444883766",
                    "api_secret", "zGypras-0D3SjN88styvg8yvFBc"));
        }
    }


