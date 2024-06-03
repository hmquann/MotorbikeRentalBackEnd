package com.MotorbikeRental.controller;

import com.MotorbikeRental.entity.Discount;
import com.MotorbikeRental.exception.ValidationException;
import com.MotorbikeRental.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/api/admin/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;


    @PostMapping("/addDiscount")
    public ResponseEntity<?> createDiscount(@RequestBody  Discount discount) {
        try{
            Discount createDiscount = discountService.createDiscount(discount);
            return ResponseEntity.ok(createDiscount);
        }catch (ValidationException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
    @GetMapping("/getAllDiscount")
    public ResponseEntity<List<Discount>> listDiscounts() {
        return ResponseEntity.ok(discountService.getAllDiscounts());
    }

    @GetMapping()
    public ResponseEntity<Discount> getDiscountByCode(@RequestParam ("code") String code) {
        Discount discount = discountService.getDiscountByCode(code);

        if(discount == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(discount);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDiscount(@RequestParam("code") String code) {
        boolean isDeleted = discountService.deleteDiscountByCode(code);
        if (!isDeleted) {
            return new ResponseEntity<>("Discount with code " + code+ " not found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Discount is deleted", HttpStatus.OK);
    }



}
