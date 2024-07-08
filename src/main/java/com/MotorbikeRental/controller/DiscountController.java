package com.MotorbikeRental.controller;

import com.MotorbikeRental.dto.DiscountDto;
import com.MotorbikeRental.dto.DiscountDtoResponse;
import com.MotorbikeRental.dto.ModelDto;
import com.MotorbikeRental.entity.Discount;
import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.exception.ValidationException;
import com.MotorbikeRental.repository.UserRepository;
import com.MotorbikeRental.service.DiscountService;
import com.MotorbikeRental.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/addDiscount")
    public ResponseEntity<?> createDiscount(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody DiscountDto discountDto) {

        try {
            String token = accessToken.split(" ")[1];
            String username = jwtService.extractUsername(token);

            Optional<User> userOptional = userRepository.findByEmail(username);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            User user = userOptional.get();

            DiscountDtoResponse createdDiscount = discountService.createDiscount(discountDto, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDiscount);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/getAllDiscount/{page}/{pageSize}")
    public ResponseEntity<Page<DiscountDtoResponse>> listDiscounts(@PathVariable int page, @PathVariable int pageSize,
                                                                   @RequestParam(required = false) List<String> roles,
                                                                   @RequestParam(required = false) Long userId
                                                                   ) {
        Page<DiscountDtoResponse> discount = discountService.getAllDiscounts(page,pageSize,roles, userId);
        return ResponseEntity.ok(discount);
    }

//    @GetMapping("{code}")
//    public ResponseEntity<Discount> getDiscountByCode(@PathVariable String code) {
//        Discount discount = discountService.getDiscountByCode(code);
//
//        if(discount == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        }
//        return ResponseEntity.ok(discount);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountDtoResponse> getDiscountById(@PathVariable Long id) {
        DiscountDtoResponse discountDto = discountService.getDiscountById(id);
        return ResponseEntity.ok(discountDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDiscount(@RequestParam("code") String code) {
        boolean isDeleted = discountService.deleteDiscountByCode(code);
        if (!isDeleted) {
            return new ResponseEntity<>("Discount with code " + code+ " not found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Discount is deleted", HttpStatus.OK);
    }

    @GetMapping("/updateExpired")
    public String updateExpiredDiscounts() {
        discountService.updateExpiredDiscounts();
        return "Expired discounts updated.";
    }



}
