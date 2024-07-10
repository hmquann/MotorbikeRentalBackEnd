package com.MotorbikeRental.service;

import com.MotorbikeRental.dto.DiscountDto;
import com.MotorbikeRental.dto.DiscountDtoResponse;
import com.MotorbikeRental.entity.Discount;
import com.MotorbikeRental.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DiscountService {

    DiscountDtoResponse createDiscount(DiscountDto discountDto, User createdBy);

    Page<DiscountDtoResponse> getAllDiscounts(int page, int pageSize, List<String> roles, Long userId);

    Discount getDiscountByCode(String code);

    boolean deleteDiscountById(Long id);

    void removeUserReferences(Long discountId);

    public void updateExpiredDiscounts();

    DiscountDtoResponse getDiscountById(Long id);

    DiscountDtoResponse updateDiscount(Long id, DiscountDto updatedDiscount);

}
