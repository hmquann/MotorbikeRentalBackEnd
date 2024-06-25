//package com.MotorbikeRental.controller;
//
//import com.MotorbikeRental.entity.License;
//import com.MotorbikeRental.entity.User;
//import com.MotorbikeRental.repository.UserRepository;
//import com.MotorbikeRental.service.JWTService;
//import com.MotorbikeRental.service.LicenseService;
//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Map;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/license")
//public class LicenseController {
//    private LicenseService licenseService;
//    @Autowired
//    private Cloudinary cloudinary;
//    @Autowired
//    private JWTService jwtService;
//
//    @Autowired
//    private UserRepository userRepository;
//    @PostMapping("/uploadLicense")
//    public ResponseEntity<License> createLicense(@RequestHeader("Authorization") String accessToken,
//            @RequestPart("license") License license,
//            @RequestPart("imageFile") MultipartFile imageFile) {
//        String token = accessToken.split(" ")[1];
//        String username = this.jwtService.extractUsername(token);
//        Optional<User> user = userRepository.findByEmail(username);
//        try {
//            // Upload the image to Cloudinary
//            Map<String, Object> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
//            String imageUrl = (String) uploadResult.get("secure_url");
//            // Set the image URL to the license
//            license.setLicenseImageUrl(imageUrl);
//            license.setUser(user.get());
//            license.setStatus(false);
//            // Return the saved license with the image URL
//            return ResponseEntity.ok(license);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to upload image", e);
//        }
//    }
//
//
//}
