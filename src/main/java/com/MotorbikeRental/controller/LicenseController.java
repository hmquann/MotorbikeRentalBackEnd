package com.MotorbikeRental.controller;

import com.MotorbikeRental.dto.LicenseDto;
import com.MotorbikeRental.dto.RegisterLicenseDto;
import com.MotorbikeRental.entity.License;
import com.MotorbikeRental.entity.LicenseStatus;
import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.exception.LicenseNotFound;
import com.MotorbikeRental.repository.LicenseRepository;
import com.MotorbikeRental.repository.UserRepository;
import com.MotorbikeRental.service.JWTService;
import com.MotorbikeRental.service.LicenseService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/license")
public class LicenseController {
    @Autowired
    private LicenseService licenseService;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LicenseRepository licenseRepository;
    @PostMapping("/uploadLicense")
    public ResponseEntity<License> createLicense(
            @RequestHeader("Authorization") String accessToken,
            @ModelAttribute RegisterLicenseDto licenseDto) {

        String token = accessToken.split(" ")[1];
        String username = jwtService.extractUsername(token);
        System.out.println(username);
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
            license.setLicenseType(licenseDto.getLicenseType());
            license.setUser(user.get());
            license.setStatus(LicenseStatus.PENDING);
            // Save the license to the database
            License savedLicense = licenseRepository.save(license);

            // Return the saved license with the image URL
            return ResponseEntity.ok(savedLicense);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }
    @PostMapping("/updateLicense")
    public ResponseEntity<License> updateLicense(
            @RequestHeader("Authorization") String accessToken,
            @ModelAttribute RegisterLicenseDto licenseDto) {

        String token = accessToken.split(" ")[1];
        String username = jwtService.extractUsername(token);
        System.out.println(username);
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            // Upload the image to Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(licenseDto.getLicenseImageFile().getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("secure_url");

            // Create a new License entity
            License license = licenseRepository.getLicenseByuserId(user.get().getId());
            license.setLicenseNumber(licenseDto.getLicenseNumber());
            license.setBirthOfDate(licenseDto.getBirthOfDate());
            license.setLicenseImageUrl(imageUrl);
            license.setLicenseType(licenseDto.getLicenseType());
            license.setUser(user.get());
            license.setStatus(LicenseStatus.PENDING);
            // Save the license to the database
            License savedLicense = licenseRepository.save(license);

            // Return the saved license with the image URL
            return ResponseEntity.ok(savedLicense);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }
  @GetMapping("getLicenseByUserId/{id}")
    public ResponseEntity<LicenseDto> getLicenseByUserId(@PathVariable Long id){
        LicenseDto licenseDto=licenseService.getLicenseByUserId(id);
        if(licenseDto == null){
            throw new LicenseNotFound("You don't have license. Please upload and verify!!!");
        }
        return ResponseEntity.ok(licenseDto);
  }
    @GetMapping("/getAllLicense/{page}/{pageSize}")
    public ResponseEntity<Page<LicenseDto>> listLicenseWithPagination(@PathVariable int page, @PathVariable int pageSize) {
        Page<LicenseDto> licensePage = licenseService.getPendingLicenseWithPagination(page,pageSize,LicenseStatus.PENDING);
        return ResponseEntity.ok(licensePage);
    }
    @PostMapping(value="/approve", consumes = "application/json")
    public void approveLicense(@RequestBody Map<String, String> payload,
                              @RequestHeader("Authorization") String accessToken) {
        String licenseNumber = payload.get("licenseNumber");
        licenseService.approveLicense(licenseNumber);
    }
    @PostMapping(value = "/reject", consumes = "application/json")
    public void rejectLicense(@RequestBody Map<String, String> payload,
                              @RequestHeader("Authorization") String accessToken) {
        String licenseNumber = payload.get("licenseNumber");
        licenseService.rejectLicense(licenseNumber);
    }
}
