package com.MotorbikeRental.controller;

import com.MotorbikeRental.config.VNPayConfig;
import com.MotorbikeRental.dto.PaymentDto;
import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    private UserService userService;

    @GetMapping("/create_payment")
    public ResponseEntity<?> createPayment(@RequestParam Long id, @RequestParam double amount) throws UnsupportedEncodingException {
        // Kiểm tra userId có tồn tại trong cơ sở dữ liệu hay không
        Optional<User> optionalUser = Optional.ofNullable(userService.getUserById(id));
        if (!optionalUser.isPresent()) {
            // Xử lý trường hợp userId không hợp lệ
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = optionalUser.get();

        long amountInVND = (long) (amount * 100); // Chuyển đổi số tiền thành đơn vị VNPay (VND)

        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amountInVND));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl + "?id=" + id + "&amount=" + amount);
        vnp_Params.put("vnp_IpAddr", VNPayConfig.getIpAddress());

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (Iterator<String> itr = fieldNames.iterator(); itr.hasNext();) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setStatus("OK");
        paymentDto.setMessage("Successfully");
        paymentDto.setUrl(paymentUrl);

        return ResponseEntity.status(HttpStatus.OK).body(paymentDto);
    }

    @GetMapping("/return")
    public ResponseEntity<PaymentDto> returnPayment(@RequestParam String vnp_ResponseCode,
                                                    @RequestParam double amount,
                                                    @RequestParam Long id) {
        if ("00".equals(vnp_ResponseCode)) {
            userService.updateUserBalance(id, amount);

            // Trả về thông tin thanh toán thành công
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setStatus("OK");
            paymentDto.setMessage("Successfully");
            paymentDto.setUrl("https://example.com/payment-success");
            return ResponseEntity.status(HttpStatus.OK).body(paymentDto);
        } else {
            // Xử lý trường hợp thanh toán thất bại
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setStatus("FAILED");
            paymentDto.setMessage("Payment failed");
            paymentDto.setUrl("https://example.com/payment-failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(paymentDto);
        }
    }
}
