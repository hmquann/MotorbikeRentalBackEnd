package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.dto.DiscountDto;
import com.MotorbikeRental.dto.DiscountDtoResponse;
import com.MotorbikeRental.dto.ModelDto;
import com.MotorbikeRental.dto.UserDto;
import com.MotorbikeRental.entity.Discount;
import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.exception.ValidationException;
import com.MotorbikeRental.repository.DiscountRepository;
import com.MotorbikeRental.repository.UserRepository;
import com.MotorbikeRental.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private final DiscountRepository discountRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private final UserRepository userRepository;

    public DiscountDtoResponse  createDiscount(DiscountDto discountDto, User createdBy) {
        if(discountRepository.existsByCode(discountDto.getCode())){
            throw new ValidationException("Discount code already exists");
        }
        Discount discount = modelMapper.map(discountDto, Discount.class);
        discount.setCreatedBy(createdBy);
        discount.setExpired(false);
        discount = discountRepository.save(discount);

        return convertToDto(discount);
    }

    private DiscountDtoResponse convertToDto(Discount discount) {
        DiscountDtoResponse discountDto = modelMapper.map(discount, DiscountDtoResponse.class);
        if (discount.getCreatedBy() != null) {
            UserDto createdByDto = modelMapper.map(discount.getCreatedBy(), UserDto.class);
            discountDto.setCreatedByUserId(discount.getCreatedBy().getId());
            discountDto.setCreatedByUserName(discount.getCreatedBy().getFirstName() + " " + discount.getCreatedBy().getLastName());
        }
        return discountDto;
    }

    public Page<DiscountDtoResponse> getAllDiscounts(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Discount> discounts = discountRepository.findAll(pageable);
        List<DiscountDtoResponse> discountPagination =  discounts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(discountPagination, pageable, discounts.getTotalElements());
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
