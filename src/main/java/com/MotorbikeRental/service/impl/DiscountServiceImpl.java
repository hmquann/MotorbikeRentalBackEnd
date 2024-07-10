package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.dto.DiscountDto;
import com.MotorbikeRental.dto.DiscountDtoResponse;
import com.MotorbikeRental.dto.UserDto;
import com.MotorbikeRental.entity.Discount;
import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.exception.ValidationException;
import com.MotorbikeRental.repository.DiscountRepository;
import com.MotorbikeRental.repository.UserRepository;
import com.MotorbikeRental.service.DiscountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    @Transactional
    public DiscountDtoResponse  createDiscount(DiscountDto discountDto, User createdBy) {
        if(discountRepository.existsByCode(discountDto.getCode())){
            throw new ValidationException("Discount code already exists");
        }
        Discount discount = modelMapper.map(discountDto, Discount.class);
        discount.setCreatedBy(createdBy);
        discount.setExpired(false);
        discount = discountRepository.save(discount);

        Iterable<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            if (user.getId().equals(createdBy.getId()) || user.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"))) {
                continue;
            }
            user.addDiscount(discount);
            userRepository.save(user);
        }

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

    public Page<DiscountDtoResponse> getAllDiscounts(int page, int pageSize, List<String> roles, Long userId) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Discount> discounts;
        if(roles.contains("ADMIN")){
            discounts = discountRepository.findAll(pageable);
        }else{
            discounts = discountRepository.findByCreatedBy(roles,userId,pageable);
        }
        List<DiscountDtoResponse> discountPagination =  discounts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(discountPagination, pageable, discounts.getTotalElements());
    }

    public Discount getDiscountByCode(String code) {
        return discountRepository.findByCode(code);
    }

    public DiscountDtoResponse getDiscountById(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found"));

        return convertToDto(discount);
    }


    public boolean deleteDiscountByCode(String code) {
        Discount discount = discountRepository.findByCode(code);
        if (discount != null) {
            discountRepository.delete(discount);
            return true;
        }
        return false;
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateExpiredDiscounts() {
        LocalDate today = LocalDate.now();
        List<Discount> expiredDiscounts = discountRepository.findByExpiredFalseAndExpirationDateBefore(today);

        for (Discount discount : expiredDiscounts) {
            discount.setExpired(true);
            discountRepository.save(discount);
        }
    }

}
