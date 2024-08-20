package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.dto.TransactionDto;
import com.MotorbikeRental.dto.UserDto;
import com.MotorbikeRental.entity.Transaction;
import com.MotorbikeRental.entity.TransactionStatus;
import com.MotorbikeRental.entity.TransactionType;
import com.MotorbikeRental.repository.TransactionRepository;
import com.MotorbikeRental.service.TransactionService;
import com.MotorbikeRental.service.UserService;
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
public class TransactionSeriveImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final  UserService userService;

    @Override
    public Page<Transaction> getTransactionByUserId(Long userId, Pageable pageable) {
        return transactionRepository.findByUsers_Id(userId,pageable);
    }

    @Override
    public List<Transaction> getTransactionByUserIdd(Long userId) {
        return transactionRepository.findByUsers_Id(userId);
    }

    public Page<TransactionDto> getPendingWithdrawals(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactions = transactionRepository.findByTypeAndStatus(TransactionType.WITHDRAW,TransactionStatus.PENDING, pageable);

        List<TransactionDto> transactionDto = transactions.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(transactionDto, pageable, transactions.getTotalElements());
    }

    public Page<TransactionDto> getWithdrawals(TransactionStatus status,int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactions = transactionRepository.findByTypeAndStatus(TransactionType.WITHDRAW,status, pageable);

        List<TransactionDto> transactionDto = transactions.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(transactionDto, pageable, transactions.getTotalElements());
    }

    public TransactionDto convertToDto(Transaction transaction) {
        TransactionDto dto = modelMapper.map(transaction, TransactionDto.class);
        if (transaction.getUsers() != null) {
            UserDto userDto = new UserDto();
            userDto.setFirstName(transaction.getUsers().getFirstName());
            userDto.setLastName(transaction.getUsers().getLastName());
            dto.setUser(userDto);
        }
        return dto;
    }
}
