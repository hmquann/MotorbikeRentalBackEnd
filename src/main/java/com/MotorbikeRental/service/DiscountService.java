package com.MotorbikeRental.service;

import com.MotorbikeRental.entity.Discount;

import java.util.List;

public interface DiscountService {

    Discount createDiscount(Discount discount);

    List<Discount> getAllDiscounts();

    Discount getDiscountByCode(String code);

    boolean deleteDiscountByCode(String code);

}
