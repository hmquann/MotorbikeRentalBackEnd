package com.MotorbikeRental.controller;

import com.MotorbikeRental.dto.LicenseDto;
import com.MotorbikeRental.entity.License;
import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.repository.LicenseRepository;
import com.MotorbikeRental.repository.UserRepository;
import com.MotorbikeRental.service.JWTService;
import com.MotorbikeRental.service.LicenseService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/license")
public class LicenseController {
    private LicenseService licenseService;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LicenseRepository licenseRepository;
    @PostMapping("/uploadLicense")
    public ResponseEntity<License> createLicense(
            @RequestHeader("Authorization") String accessToken,
            @ModelAttribute LicenseDto licenseDto) {
        System.out.println(accessToken);
        String token = accessToken.split(" ")[0];
        String username = jwtService.extractUsername(token);
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            // Upload the image to Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(licenseDto.getLicenseImageFile().getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("secure_url");

            // Create a new License entity
            License license = new License();
            license.setLicenseNumber(licenseDto.getLicenseNumber());
            license.setBirthOfDate(licenseDto.getBirthOfDate());
            license.setLicenseImageUrl(imageUrl);
            license.setUser(user.get());
            license.setStatus(false);

            // Save the license to the database
            License savedLicense = licenseRepository.save(license);

            // Return the saved license with the image URL
            return ResponseEntity.ok(savedLicense);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }


}
