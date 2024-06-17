package com.MotorbikeRental.controller;

import com.MotorbikeRental.dto.PaymentDto;
import com.MotorbikeRental.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/create_payment")
    public ResponseEntity<?> createPayment(@RequestParam Long id, @RequestParam double amount) throws UnsupportedEncodingException {
        return paymentService.createPayment(id, amount);
    }

    @GetMapping("/return")
    public ResponseEntity<Void> returnPayment(@RequestParam String vnp_ResponseCode,
                                              @RequestParam double amount,
                                              @RequestParam Long id,
                                              @RequestParam String vnp_TxnRef  ) {
        return paymentService.returnPayment(vnp_ResponseCode, amount, id, vnp_TxnRef);
    }
}
