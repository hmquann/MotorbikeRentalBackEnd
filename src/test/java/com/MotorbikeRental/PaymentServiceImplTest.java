package com.MotorbikeRental;

import com.MotorbikeRental.config.VNPayConfig;
import com.MotorbikeRental.dto.PaymentDto;
import com.MotorbikeRental.entity.Transaction;
import com.MotorbikeRental.entity.TransactionStatus;
import com.MotorbikeRental.entity.TransactionType;
import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.repository.TransactionRepository;
import com.MotorbikeRental.service.UserService;
import com.MotorbikeRental.service.impl.PaymentServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private ServletRequestAttributes attributes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePayment_UserNotFound() throws UnsupportedEncodingException {
        // Setup
        when(userService.getUserById(1L)).thenReturn(null);

        // Action
        ResponseEntity<?> response = paymentService.createPayment(1L, BigDecimal.valueOf(1000));

        // Assertion
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void testCreatePayment_Success() throws Exception {
        // Setup
        User user = new User();
        user.setId(1L);

        when(userService.getUserById(1L)).thenReturn(user);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        // Mock HttpServletRequest and RequestContextHolder
        when(mockRequest.getHeader("X-FORWARDED-FOR")).thenReturn(null);
        when(mockRequest.getLocalAddr()).thenReturn("127.0.0.1");

        when(attributes.getRequest()).thenReturn(mockRequest);

        // Set the mocked RequestContextHolder
        RequestContextHolder.setRequestAttributes(attributes);

        // Action
        ResponseEntity<?> response = paymentService.createPayment(1L, BigDecimal.valueOf(1000));

        // Assertion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof PaymentDto);
        PaymentDto paymentDto = (PaymentDto) response.getBody();
        assertEquals("OK", paymentDto.getStatus());
        assertEquals("Successfully", paymentDto.getMessage());
        assertTrue(paymentDto.getUrl().contains(VNPayConfig.vnp_PayUrl));
    }

    @Test
    void testReturnPayment_Success() {
        // Setup
        Transaction transaction = new Transaction();
        transaction.setVnpTxnRef("12345678");
        transaction.setProcessed(false);

        when(transactionRepository.findByVnpTxnRef("12345678")).thenReturn(Optional.of(transaction));

        // Action
        ResponseEntity<Void> response = paymentService.returnPayment("00", BigDecimal.valueOf(1000), 1L, "12345678");

        // Assertion
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        List<String> locations = response.getHeaders().get("Location");
        assertNotNull(locations, "Location header should not be null");
        assertTrue(locations.stream().anyMatch(location -> location.contains("payment-success")),
                "Location header should contain 'payment-success'");

        verify(transactionRepository).save(transaction);
        assertTrue(transaction.isProcessed(), "Transaction should be marked as processed");
        assertEquals(TransactionStatus.SUCCESS, transaction.getStatus(), "Transaction status should be SUCCESS");

        verify(userService, times(1)).updateUserBalance(1L, BigDecimal.valueOf(1000));
    }

    @Test
    void testReturnPayment_Failure() {
        // Setup
        Transaction transaction = new Transaction();
        transaction.setVnpTxnRef("12345678");
        transaction.setProcessed(false);

        when(transactionRepository.findByVnpTxnRef("12345678")).thenReturn(Optional.of(transaction));

        // Action
        ResponseEntity<Void> response = paymentService.returnPayment("01", BigDecimal.valueOf(1000), 1L, "12345678");

        // Assertion
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertFalse(response.getHeaders().get("Location").contains("payment-failed"));
        assertTrue(transaction.isProcessed());
        assertEquals(TransactionStatus.FAILED, transaction.getStatus());
        verify(userService, never()).updateUserBalance(anyLong(), any(BigDecimal.class));
    }

    @Test
    void testReturnPayment_TransactionNotFound() {
        // Setup
        when(transactionRepository.findByVnpTxnRef("12345678")).thenReturn(Optional.empty());

        // Action
        ResponseEntity<Void> response = paymentService.returnPayment("00", BigDecimal.valueOf(1000), 1L, "12345678");

        // Assertion
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        List<String> locations = response.getHeaders().get("Location");
        assertNotNull(locations, "Location header should not be null");
        assertTrue(locations.stream().anyMatch(location -> location.contains("payment-failed")), "Location header should contain 'payment-failed'");

        verify(userService, never()).updateUserBalance(anyLong(), any(BigDecimal.class));
    }
}
