package com.MotorbikeRental.service;

import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

public interface PaymentService {
    ResponseEntity<?> createPayment(Long id, BigDecimal amount) throws UnsupportedEncodingException;
//    ResponseEntity<Void> returnPayment(String vnp_ResponseCode, double amount, Long id);
    ResponseEntity<Void> returnPayment(String vnp_ResponseCode, BigDecimal amount, Long id, String vnp_TxnRef);
}
