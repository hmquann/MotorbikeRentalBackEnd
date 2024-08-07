package com.MotorbikeRental.controller;
import com.MotorbikeRental.dto.*;
import com.MotorbikeRental.entity.*;
import com.MotorbikeRental.repository.ModelRepository;
import com.MotorbikeRental.repository.UserRepository;
import com.MotorbikeRental.service.*;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import com.MotorbikeRental.dto.ModelDto;
import com.MotorbikeRental.entity.Brand;
import com.MotorbikeRental.entity.Motorbike;
import com.MotorbikeRental.service.MotorbikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/motorbike")
public class MotorbikeController {

    @Autowired
    private MotorbikeService motorbikeService;

    private static final Logger logger = LoggerFactory.getLogger(MotorbikeController.class);


    @Autowired
    private MotorbikeImageService motorbikeImageService;
    @Autowired
    private Cloudinary cloudinary;

    @GetMapping("/allMotorbike/{page}/{pageSize}")
    public Page<MotorbikeDto> getAllMotorbike(@PathVariable int page,
                                                      @PathVariable int pageSize,
                                                      @RequestParam(required = false) Long userId,
                                                      @RequestParam(required = false) List<String> role,
                                                      @RequestParam String status) {
        return motorbikeService.getAllMotorbike(page,pageSize,userId,role,status);
    }

//    @GetMapping("/allMotorbike/{page}/{pageSize}")
//    public ResponseEntity<Page<Motorbike>> listBrandWithPagination(@PathVariable int page, @PathVariable int pageSize) {
//        Page<Motorbike> motorbikePage = motorbikeService.getMotorbikeWithPagination(page,pageSize);
//        return ResponseEntity.ok(motorbikePage);
//    }

    @GetMapping("/search")
    public Page<MotorbikeDto> searchByPlate(
            @RequestParam String searchTerm,
            @RequestParam String status,
            @RequestParam Long userId,
            @RequestParam List<String> role,
            @RequestParam int page,
            @RequestParam int size) {
        return motorbikeService.searchByPlate(searchTerm,status,userId,role, page, size);
    }

    @PutMapping("/toggleStatus/{id}")
    public ResponseEntity<String> toggleMotorbikeStatus(@PathVariable Long id) {
        try {
            motorbikeService.toggleMotorbikeStatus(id);
            return ResponseEntity.ok("Toggle motorbike status successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error toggling motorbike status: " + e.getMessage());
        }
    }

    @GetMapping("/pending/{page}/{pageSize}")
    public Page<MotorbikeDto> getPendingMotorbikes(@PathVariable int page,@PathVariable int pageSize){
        return motorbikeService.getPendingMotorbikes(MotorbikeStatus.PENDING,page,pageSize);
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<Motorbike> approveMotorbike(@PathVariable Long id) {
        Motorbike approvedMotorbike = motorbikeService.approveMotorbike(id);
        return ResponseEntity.ok(approvedMotorbike);
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<Motorbike> rejectMotorbike(@PathVariable Long id) {
        Motorbike approvedMotorbike = motorbikeService.rejectMotorbike(id);
        return ResponseEntity.ok(approvedMotorbike);
    }
    @RequestMapping (value="/register",method =RequestMethod.POST)
    public ResponseEntity<Motorbike> registerMotorbike( @RequestHeader("Authorization") String accessToken,@ModelAttribute RegisterMotorbikeDto registerMotorbikeDto) {
        System.out.println(registerMotorbikeDto);

        Motorbike savedMotorbike=motorbikeService.registerMotorbike(accessToken,registerMotorbikeDto);
        Long motorbikeId = savedMotorbike.getId();
        List<MultipartFile> imageFiles = registerMotorbikeDto.getMotorbikeImages();
        List<String>imageUrls=new ArrayList<>();
        try {
            for(MultipartFile file:imageFiles) {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
               imageUrls.add((String) uploadResult.get("secure_url"));
            }
        }  catch (IOException e) {
           return ResponseEntity.internalServerError().body(null);
        }
        motorbikeImageService.saveMotorbikeImage(imageUrls,motorbikeId);
        return ResponseEntity.ok(savedMotorbike);
    }
    @GetMapping("/activeMotorbikeList")
    public List<MotorbikeDto> getAllActiveMotorbike(){
        return motorbikeService.getAllMotorbikeByStatus(MotorbikeStatus.ACTIVE);
    }
    @PostMapping(value="/filter/{page}/{pageSize}", consumes = "application/json", produces = "application/json")
    public Page<MotorbikeDto>getMotorbikeByFilter(@RequestBody FilterMotorbikeDto filter,@PathVariable int page,@PathVariable int pageSize){
        return motorbikeService.listMotorbikeByFilter(filter,page,pageSize);
    }
    @PostMapping("/updateMotorbike/{id}")
    public MotorbikeDto updateMotorbike(@PathVariable Long id,@RequestBody UpdateMotorbikeDto updateMotorbikeDto){
        return motorbikeService.updateMotorbike(id,updateMotorbikeDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotorbikeDto> getMotorbikeById(@PathVariable Long id) {
        MotorbikeDto motorbikeDto = motorbikeService.getMotorbikeById(id);
        return ResponseEntity.ok(motorbikeDto);
    }

//    @GetMapping("/getMotorbikeById/{id}")
//    public ResponseEntity<MotorbikeDto> getMotorbikeById(@PathVariable Long id){
//        return ResponseEntity.ok(motorbikeService.getMotorbikeById(id));
//    }


    @GetMapping("/existMotorbikeByUserId/{motorbikeId}/{userId}")
    public ResponseEntity<MotorbikeDto> existMotorbikeByUserId(@PathVariable Long motorbikeId, @PathVariable Long userId) {
        try {
            MotorbikeDto motorbikeDto = motorbikeService.existMotorbikeByUserId(motorbikeId, userId);
            if (motorbikeDto == null) {
                logger.error("Motorbike not found for motorbikeId: {} and userId: {}", motorbikeId, userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(motorbikeDto);
        } catch (Exception e) {
            logger.error("An error occurred while checking motorbike by user ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/{id}/motorbikes")
    public List<MotorbikeDto> getUserMotorbikes(@PathVariable Long id) {
        return motorbikeService.getMotorbikeByUserId(id);
    }


}




