package com.MotorbikeRental.controller;
import com.MotorbikeRental.dto.*;
import com.MotorbikeRental.entity.*;
import com.MotorbikeRental.repository.ModelRepository;
import com.MotorbikeRental.repository.UserRepository;
import com.MotorbikeRental.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/motorbike")
public class MotorbikeController {

    @Autowired
    private MotorbikeService motorbikeService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelRepository modelRepository;

    @GetMapping("/allMotorbike/{page}/{pageSize}")
    public Page<RegisterMotorbikeDto> getAllMotorbike(@PathVariable int page,@PathVariable int pageSize) {
        return motorbikeService.getAllMotorbike(page,pageSize);
    }

//    @GetMapping("/allMotorbike/{page}/{pageSize}")
//    public ResponseEntity<Page<Motorbike>> listBrandWithPagination(@PathVariable int page, @PathVariable int pageSize) {
//        Page<Motorbike> motorbikePage = motorbikeService.getMotorbikeWithPagination(page,pageSize);
//        return ResponseEntity.ok(motorbikePage);
//    }

    @GetMapping("/search")
    public Page<RegisterMotorbikeDto> searchByPlate(
            @RequestParam String searchTerm,
            @RequestParam int page,
            @RequestParam int size) {
        return motorbikeService.searchByPlate(searchTerm, page, size);
    }

    @PutMapping("/toggleStatus/{id}")
    public ResponseEntity<String> toggleMotorbikeStatus(@PathVariable Long id) {
        try {
            // Gọi phương thức từ service để thực hiện toggle trạng thái của xe máy
            motorbikeService.toggleMotorbikeStatus(id);
            return ResponseEntity.ok("Toggle motorbike status successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error toggling motorbike status: " + e.getMessage());
        }
    }

    @GetMapping("/pending/{page}/{pageSize}")
    public Page<RegisterMotorbikeDto> getPendingMotorbikes(@PathVariable int page,@PathVariable int pageSize){
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
    public ResponseEntity<Motorbike> registerMotorbike( @RequestBody RegisterMotorbikeDto registerMotorbikeDto){
        Motorbike motorbike=new Motorbike();
        motorbikeService.registerMotorbike(registerMotorbikeDto);
        return ResponseEntity.ok(motorbike);
    }
    @GetMapping("/activeMotorbikeList/{page}/{pageSize}")
    public Page<ListActiveMotorbikeDto> getAllActiveMotorbike(@PathVariable int page,@PathVariable int pageSize){
        return motorbikeService.getAllMotorbikeByStatus(MotorbikeStatus.ACTIVE,page,pageSize);
    }
}




