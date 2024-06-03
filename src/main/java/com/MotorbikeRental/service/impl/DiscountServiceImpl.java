package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.entity.Discount;
import com.MotorbikeRental.exception.ValidationException;
import com.MotorbikeRental.repository.DiscountRepository;
import com.MotorbikeRental.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;

    public Discount createDiscount(Discount discount) {
        if(discountRepository.existsByCode(discount.getCode())){
            throw new ValidationException("Discount code already exists");
        }

        return discountRepository.save(discount);
    }

    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    public Discount getDiscountByCode(String code) {
        return discountRepository.findByCode(code);
    }


    public boolean deleteDiscountByCode(String code) {
        Discount discount = discountRepository.findByCode(code);
        if (discount != null) {
            discountRepository.delete(discount);
            return true;
        }
        return false;
    }

}
