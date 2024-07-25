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
        DiscountValidation(discountDto);
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
//            UserDto createdByDto = modelMapper.map(discount.getCreatedBy(), UserDto.class);
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

    public void removeUserReferences(Long discountId) {
        Discount discount = discountRepository.findById(discountId).orElseThrow(() -> new RuntimeException("Discount not found"));
        for (User user : discount.getUsers()) {
            user.getDiscounts().remove(discount);
            userRepository.save(user);
        }
    }


    public boolean deleteDiscountById(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found"));
        if (discount != null) {
            discountRepository.delete(discount);
            return true;
        }
        return false;
    }
    @Override
    public boolean deleteDiscountByIdAndUserId(Long id, Long userId) {
        Discount discount = discountRepository.findByDiscountIdAndUserId(id, userId);
        if (discount != null) {
            discountRepository.delete(discount);
            return true;
        }
        return false;
    }
    public DiscountDtoResponse  updateDiscount(Long id, DiscountDto discountDto) {
        Discount existingDiscount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found"));


        if (!existingDiscount.getCode().equals(discountDto.getCode())) {
            if (discountRepository.existsByCode(discountDto.getCode())) {
                throw new ValidationException("Discount code already exists");
            }
            existingDiscount.setCode(discountDto.getCode());
        }

        DiscountValidation(discountDto);

        existingDiscount.setName(discountDto.getName());
        existingDiscount.setDescription(discountDto.getDescription());
        existingDiscount.setVoucherType(discountDto.getVoucherType());
        switch (existingDiscount.getVoucherType()) {
            case PERCENTAGE:
                existingDiscount.setDiscountPercent(discountDto.getDiscountPercent());
                existingDiscount.setMaxDiscountMoney(discountDto.getMaxDiscountMoney());
                existingDiscount.setDiscountMoney(0);
                break;
            case FIXED_MONEY:
                existingDiscount.setDiscountMoney(discountDto.getDiscountMoney());
                existingDiscount.setDiscountPercent(0);
                existingDiscount.setMaxDiscountMoney(0);
                break;
            default:
                throw new ValidationException("Invalid voucher type");
        }
        existingDiscount.setStartDate(discountDto.getStartDate());
        existingDiscount.setExpirationDate(discountDto.getExpirationDate());
        existingDiscount.setQuantity(discountDto.getQuantity());


        Discount updatedDiscount = discountRepository.save(existingDiscount);
        return convertToDto(updatedDiscount);
    }

    @Override
    public List<DiscountDtoResponse> getListDiscountByUser(Long id) {
        User user = userRepository.getUserById(id);
        List<Discount> discountList = discountRepository.findDiscountsByUserId(id);
        List<DiscountDtoResponse> discountDtoResponseList =  discountList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return discountDtoResponseList;
    }



    private void DiscountValidation(DiscountDto discountDto) {
        if (discountDto.getDiscountPercent() < 0 || discountDto.getDiscountPercent() > 100) {
            throw new ValidationException("Discount percent must be between 0 and 100");
        }
        if (discountDto.getDiscountMoney() < 0 || discountDto.getMaxDiscountMoney() < 0) {
            throw new ValidationException("Discount money must be a positive value");
        }

        if (discountDto.getStartDate() == null || discountDto.getExpirationDate() == null) {
            throw new ValidationException("Start date and end date must not be null");
        }
        if (discountDto.getExpirationDate().isBefore(discountDto.getStartDate())) {
            throw new ValidationException("End date must be after start date");
        }

        if (discountDto.getQuantity() <= 0) {
            throw new ValidationException("Quantity must be greater than 0");
        }
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
