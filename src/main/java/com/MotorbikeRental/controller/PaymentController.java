package com.MotorbikeRental.controller;

import com.MotorbikeRental.dto.PaymentDto;
import com.MotorbikeRental.service.PaymentService;
import com.MotorbikeRental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @GetMapping("/create_payment")
    public ResponseEntity<?> createPayment(@RequestParam Long id, @RequestParam BigDecimal amount) throws UnsupportedEncodingException {
        return paymentService.createPayment(id, amount);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestParam Long id, @RequestParam BigDecimal amount,
                                      @RequestParam String accountNumber,
                                      @RequestParam String bankName) throws UnsupportedEncodingException {
        try {
            userService.withdrawMoney(id, amount, accountNumber, bankName);
            return ResponseEntity.ok("Request Withdrawal successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Withdrawal failed: " + e.getMessage());
        }
    }

    @PostMapping("/approveWithdrawal")
    public ResponseEntity<?> approveWithdrawal(@RequestParam Long transactionId) {
        try {
            userService.approveWithdrawal(transactionId);
            return ResponseEntity.ok("Withdrawal approved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Approval failed: " + e.getMessage());
        }
    }

    @PostMapping("/subtract")
    public ResponseEntity<String> subtract(@RequestParam Long senderId, @RequestParam Long receiverId,
                                           @RequestParam BigDecimal amount, @RequestParam String motorbikeName,
                                           @RequestParam String motorbikePlate) {
        try {
            userService.subtractMoney(senderId, receiverId, amount, motorbikeName, motorbikePlate);
            return ResponseEntity.ok("Subtraction successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Subtraction failed: " + e.getMessage());
        }
    }

    @PostMapping("/refund")
    public ResponseEntity<String> refund(@RequestParam Long senderId, @RequestParam Long receiverId,
                                           @RequestParam BigDecimal amount, @RequestParam String motorbikeName,
                                           @RequestParam String motorbikePlate) {
        try {
            userService.refundSubtractMoney(senderId, receiverId, amount, motorbikeName, motorbikePlate);
            return ResponseEntity.ok("Refund successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Refund failed: " + e.getMessage());
        }
    }

    @PostMapping("/punish")
    public ResponseEntity<String> punish(@RequestParam Long senderId, @RequestParam Long receiverId,
                                         @RequestParam BigDecimal amount, @RequestParam String motorbikeName,
                                         @RequestParam String motorbikePlate) {
        try {
            userService.punishSubtractMoney(senderId, receiverId, amount, motorbikeName, motorbikePlate);
            return ResponseEntity.ok("Punish successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Refund failed: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestParam Long id, @RequestParam BigDecimal amount,
                                      @RequestParam String motorbikeName,
                                      @RequestParam String motorbikePlate) {
        try {
            String transactionCode = userService.generateTransactionCode(id);
            userService.addMoney(id, amount, motorbikeName, motorbikePlate, transactionCode);
            return ResponseEntity.ok("Addition successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Addition failed: " + e.getMessage());
        }
    }

    @GetMapping("/return")
    public ResponseEntity<Void> returnPayment(@RequestParam String vnp_ResponseCode,
                                              @RequestParam BigDecimal amount,
                                              @RequestParam Long id,
                                              @RequestParam String vnp_TxnRef  ) {
        return paymentService.returnPayment(vnp_ResponseCode, amount, id, vnp_TxnRef);
    }
}
