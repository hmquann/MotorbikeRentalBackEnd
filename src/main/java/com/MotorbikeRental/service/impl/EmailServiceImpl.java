package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.dto.EmailCancelBookingDto;
import com.MotorbikeRental.dto.EmailCancelBookingForLessorDto;
import com.MotorbikeRental.dto.EmailSuccessBookingDto;
import com.MotorbikeRental.dto.EmailSuccessBookingForLessorDto;
import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.service.EmailService;
import com.MotorbikeRental.service.UserService;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.NumberFormat;
import java.util.Locale;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;


    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    private final UserService userService;
    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine,UserService userService) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.userService = userService;
    }
    @Override
    public String sendVerificationEmail(User user, String url) {
        try{

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setSubject("Verify Registration");

            Context context = new Context();
            context.setVariable("userName",user.getFirstName()+ user.getLastName());
            context.setVariable("url",url);
            String htmlContent = templateEngine.process("registerTemplate", context);
            mimeMessageHelper.setText(htmlContent, true);


            javaMailSender.send(mimeMessage);
            return "mail send";
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendMail(MultipartFile[] file, String to, String[] cc, String subject, String body) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setCc(cc);
            mimeMessageHelper.setSubject(subject);

            Context context = new Context();
            context.setVariable("subject", subject);
            context.setVariable("body", body);
//            String otp = OTPGeneratorService.generateOTP();
//            context.setVariable("otp", otp); // Include OTP in the email body

            String htmlContent = templateEngine.process("registerTemplate", context);
            mimeMessageHelper.setText(htmlContent, true);

            for(int i = 0; i< file.length; i++){
                mimeMessageHelper.addAttachment(
                        file[i].getOriginalFilename(),
                        new ByteArrayResource(file[i].getBytes())
                );
            }

            javaMailSender.send(mimeMessage);
            return "mail send";
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSiteUrl(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        return url.replace(request.getServletPath(),"");
    }

    @Override
    public String sendForgotPasswordEmail(User user,String url) {
        try{

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setSubject("Forgot Password");

            Context context = new Context();
            context.setVariable("userName",user.getFirstName()+ user.getLastName());
            context.setVariable("url",url);
            String htmlContent = templateEngine.process("forgotPasswordTemplate", context);
            mimeMessageHelper.setText(htmlContent, true);


            javaMailSender.send(mimeMessage);
            return "mail send";
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendChangeEmail(User user, String url,String newEmail) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(newEmail);
            mimeMessageHelper.setSubject("Email Change Notification");

            Context context = new Context();
            context.setVariable("userName",user.getFirstName()+ user.getLastName());
            context.setVariable("url",url);
            String htmlContent = templateEngine.process("changeEmailTemplate", context);
            mimeMessageHelper.setText(htmlContent, true);


            javaMailSender.send(mimeMessage);
            return "mail send";
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendEmailSuccessBooking(EmailSuccessBookingDto emailSuccessBookingDto) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(emailSuccessBookingDto.getRenterEmail());
            mimeMessageHelper.setSubject("Xác nhận đặt chuyến");

            Context context = new Context();
            context.setVariable("userName", emailSuccessBookingDto.getRenterName());
            context.setVariable("motorbikeName", emailSuccessBookingDto.getMotorbikeName());
            context.setVariable("motorbikePlate", emailSuccessBookingDto.getMotorbikePlate());
            context.setVariable("bookingTime", emailSuccessBookingDto.getBookingTime());
            context.setVariable("startDate", emailSuccessBookingDto.getStartDate());
            context.setVariable("endDate", emailSuccessBookingDto.getEndDate());
            context.setVariable("receiveLocation", emailSuccessBookingDto.getReceiveLocation());

            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedTotalPrice = currencyFormatter.format(emailSuccessBookingDto.getTotalPrice());
            context.setVariable("totalPrice", formattedTotalPrice);

            String htmlContent = templateEngine.process("sendEmailSuccessBooking", context);
            mimeMessageHelper.setText(htmlContent, true);


            javaMailSender.send(mimeMessage);
            return "mail send";
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendEmailSuccessBookingForLessor(EmailSuccessBookingForLessorDto emailSuccessBookingForLessorDto) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(emailSuccessBookingForLessorDto.getLessorEmail());
            mimeMessageHelper.setSubject("Yêu Cầu Đặt Xe Mới");

            Context context = new Context();
            context.setVariable("userName", emailSuccessBookingForLessorDto.getLessorName());
            context.setVariable("renterName", emailSuccessBookingForLessorDto.getRenterName());
            context.setVariable("motorbikeName", emailSuccessBookingForLessorDto.getMotorbikeName());
            context.setVariable("motorbikePlate", emailSuccessBookingForLessorDto.getMotorbikePlate());
            context.setVariable("bookingTime", emailSuccessBookingForLessorDto.getBookingTime());
            context.setVariable("startDate", emailSuccessBookingForLessorDto.getStartDate());
            context.setVariable("endDate", emailSuccessBookingForLessorDto.getEndDate());
            context.setVariable("receiveLocation", emailSuccessBookingForLessorDto.getReceiveLocation());

            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedTotalPrice = currencyFormatter.format(emailSuccessBookingForLessorDto.getTotalPrice());
            context.setVariable("totalPrice", formattedTotalPrice);

            String htmlContent = templateEngine.process("sendEmailSuccessBookingForLessor", context);
            mimeMessageHelper.setText(htmlContent, true);


            javaMailSender.send(mimeMessage);
            return "mail send";
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendEmailCancleBooking(EmailCancelBookingDto emailCancelBookingDto) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(emailCancelBookingDto.getRenterEmail());
            mimeMessageHelper.setSubject("Thông Báo Hủy Chuyến");

            Context context = new Context();
            context.setVariable("userName", emailCancelBookingDto.getRenterName());
            context.setVariable("motorbikeName", emailCancelBookingDto.getMotorbikeName());
            context.setVariable("motorbikePlate", emailCancelBookingDto.getMotorbikePlate());
            context.setVariable("bookingTime", emailCancelBookingDto.getBookingTime());
            context.setVariable("startDate", emailCancelBookingDto.getStartDate());
            context.setVariable("endDate", emailCancelBookingDto.getEndDate());
            context.setVariable("receiveLocation", emailCancelBookingDto.getReceiveLocation());

            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedTotalPrice = currencyFormatter.format(emailCancelBookingDto.getTotalPrice());
            context.setVariable("totalPrice", formattedTotalPrice);
            context.setVariable("reason", emailCancelBookingDto.getReason());
            String htmlContent = templateEngine.process("sendEmailCancelBooking", context);
            mimeMessageHelper.setText(htmlContent, true);


            javaMailSender.send(mimeMessage);
            return "mail send";
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendEmailCancleBookingForLessor(EmailCancelBookingForLessorDto emailCancelBookingForLessorDto) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(emailCancelBookingForLessorDto.getLessorEmail());
            mimeMessageHelper.setSubject("Thông Báo Hủy Chuyến");

            Context context = new Context();
            context.setVariable("userName", emailCancelBookingForLessorDto.getLessorName());
            context.setVariable("renterName", emailCancelBookingForLessorDto.getRenterName());
            context.setVariable("motorbikeName", emailCancelBookingForLessorDto.getMotorbikeName());
            context.setVariable("motorbikePlate", emailCancelBookingForLessorDto.getMotorbikePlate());
            context.setVariable("bookingTime", emailCancelBookingForLessorDto.getBookingTime());
            context.setVariable("startDate", emailCancelBookingForLessorDto.getStartDate());
            context.setVariable("endDate", emailCancelBookingForLessorDto.getEndDate());
            context.setVariable("receiveLocation", emailCancelBookingForLessorDto.getReceiveLocation());

            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedTotalPrice = currencyFormatter.format(emailCancelBookingForLessorDto.getTotalPrice());
            context.setVariable("totalPrice", formattedTotalPrice);
            context.setVariable("reason", emailCancelBookingForLessorDto.getReason());
            String htmlContent = templateEngine.process("sendEmailCancelBookingForLessor", context);
            mimeMessageHelper.setText(htmlContent, true);


            javaMailSender.send(mimeMessage);
            return "mail send";
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendEmailRejectBooking(EmailCancelBookingDto emailCancelBookingDto) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(emailCancelBookingDto.getRenterEmail());
            mimeMessageHelper.setSubject("Thông Báo Từ Chối Chuyến");

            Context context = new Context();
            context.setVariable("userName", emailCancelBookingDto.getRenterName());
            context.setVariable("motorbikeName", emailCancelBookingDto.getMotorbikeName());
            context.setVariable("motorbikePlate", emailCancelBookingDto.getMotorbikePlate());
            context.setVariable("bookingTime", emailCancelBookingDto.getBookingTime());
            context.setVariable("startDate", emailCancelBookingDto.getStartDate());
            context.setVariable("endDate", emailCancelBookingDto.getEndDate());
            context.setVariable("receiveLocation", emailCancelBookingDto.getReceiveLocation());

            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedTotalPrice = currencyFormatter.format(emailCancelBookingDto.getTotalPrice());
            context.setVariable("totalPrice", formattedTotalPrice);
            context.setVariable("reason", emailCancelBookingDto.getReason());
            String htmlContent = templateEngine.process("sendEmailRejectBooking", context);
            mimeMessageHelper.setText(htmlContent, true);


            javaMailSender.send(mimeMessage);
            return "mail send";
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendEmailRejectBookingForLessor(EmailCancelBookingForLessorDto emailCancelBookingForLessorDto) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(emailCancelBookingForLessorDto.getLessorEmail());
            mimeMessageHelper.setSubject("Thông Báo Từ Chối Chuyến");

            Context context = new Context();
            context.setVariable("userName", emailCancelBookingForLessorDto.getLessorName());
            context.setVariable("renterName", emailCancelBookingForLessorDto.getRenterName());
            context.setVariable("motorbikeName", emailCancelBookingForLessorDto.getMotorbikeName());
            context.setVariable("motorbikePlate", emailCancelBookingForLessorDto.getMotorbikePlate());
            context.setVariable("bookingTime", emailCancelBookingForLessorDto.getBookingTime());
            context.setVariable("startDate", emailCancelBookingForLessorDto.getStartDate());
            context.setVariable("endDate", emailCancelBookingForLessorDto.getEndDate());
            context.setVariable("receiveLocation", emailCancelBookingForLessorDto.getReceiveLocation());

            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedTotalPrice = currencyFormatter.format(emailCancelBookingForLessorDto.getTotalPrice());
            context.setVariable("totalPrice", formattedTotalPrice);
            context.setVariable("reason", emailCancelBookingForLessorDto.getReason());
            String htmlContent = templateEngine.process("sendEmailRejectBookingForLessor", context);
            mimeMessageHelper.setText(htmlContent, true);


            javaMailSender.send(mimeMessage);
            return "mail send";
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }



    @Override
    public String sendEmailApproveBooking(EmailSuccessBookingDto emailSuccessBookingDto) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(emailSuccessBookingDto.getRenterEmail());
            mimeMessageHelper.setSubject("Yêu Cầu Thuê Xe Đã Được Duyệt");

            Context context = new Context();
            context.setVariable("userName", emailSuccessBookingDto.getRenterName());
            context.setVariable("motorbikeName", emailSuccessBookingDto.getMotorbikeName());
            context.setVariable("motorbikePlate", emailSuccessBookingDto.getMotorbikePlate());
            context.setVariable("bookingTime", emailSuccessBookingDto.getBookingTime());
            context.setVariable("startDate", emailSuccessBookingDto.getStartDate());
            context.setVariable("endDate", emailSuccessBookingDto.getEndDate());
            context.setVariable("receiveLocation", emailSuccessBookingDto.getReceiveLocation());

            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedTotalPrice = currencyFormatter.format(emailSuccessBookingDto.getTotalPrice());
            context.setVariable("totalPrice", formattedTotalPrice);
            String htmlContent = templateEngine.process("sendEmailApproveBooking", context);
            mimeMessageHelper.setText(htmlContent, true);


            javaMailSender.send(mimeMessage);
            return "mail send";
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendEmailApproveBookingForLessor(EmailSuccessBookingForLessorDto emailSuccessBookingForLessorDto) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(emailSuccessBookingForLessorDto.getLessorEmail());
            mimeMessageHelper.setSubject("Duyệt Thành Công Yêu Cầu Thuê Xe");

            Context context = new Context();
            context.setVariable("userName", emailSuccessBookingForLessorDto.getLessorName());
            context.setVariable("renterName", emailSuccessBookingForLessorDto.getRenterName());
            context.setVariable("motorbikeName", emailSuccessBookingForLessorDto.getMotorbikeName());
            context.setVariable("motorbikePlate", emailSuccessBookingForLessorDto.getMotorbikePlate());
            context.setVariable("bookingTime", emailSuccessBookingForLessorDto.getBookingTime());
            context.setVariable("startDate", emailSuccessBookingForLessorDto.getStartDate());
            context.setVariable("endDate", emailSuccessBookingForLessorDto.getEndDate());
            context.setVariable("receiveLocation", emailSuccessBookingForLessorDto.getReceiveLocation());

            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedTotalPrice = currencyFormatter.format(emailSuccessBookingForLessorDto.getTotalPrice());
            context.setVariable("totalPrice", formattedTotalPrice);
            String htmlContent = templateEngine.process("sendEmailApproveBookingForLessor", context);
            mimeMessageHelper.setText(htmlContent, true);


            javaMailSender.send(mimeMessage);
            return "mail send";
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendEmailDepositMadeBooking(EmailSuccessBookingDto emailSuccessBookingDto) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(emailSuccessBookingDto.getRenterEmail());
            mimeMessageHelper.setSubject("Thông Báo Đặt Cọc Thành Công");

            Context context = new Context();
            context.setVariable("userName", emailSuccessBookingDto.getRenterName());
            context.setVariable("motorbikeName", emailSuccessBookingDto.getMotorbikeName());
            context.setVariable("motorbikePlate", emailSuccessBookingDto.getMotorbikePlate());
            context.setVariable("bookingTime", emailSuccessBookingDto.getBookingTime());
            context.setVariable("startDate", emailSuccessBookingDto.getStartDate());
            context.setVariable("endDate", emailSuccessBookingDto.getEndDate());
            context.setVariable("receiveLocation", emailSuccessBookingDto.getReceiveLocation());

            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedTotalPrice = currencyFormatter.format(emailSuccessBookingDto.getTotalPrice());
            context.setVariable("totalPrice", formattedTotalPrice);
            String formattedDepositPrice = currencyFormatter.format(emailSuccessBookingDto.getTotalPrice() * 30/ 100);
            context.setVariable("depositPrice", formattedDepositPrice);
            String htmlContent = templateEngine.process("sendEmailDepositMadeBooking", context);
            mimeMessageHelper.setText(htmlContent, true);


            javaMailSender.send(mimeMessage);
            return "mail send";
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendEmailDepositMadeBookingForLessor(EmailSuccessBookingForLessorDto emailSuccessBookingForLessorDto) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(emailSuccessBookingForLessorDto.getLessorEmail());
            mimeMessageHelper.setSubject("Thông Báo Xe Đã Được Đặt Cọc Thành Công");

            Context context = new Context();
            context.setVariable("userName", emailSuccessBookingForLessorDto.getLessorName());
            context.setVariable("renterName", emailSuccessBookingForLessorDto.getRenterName());
            context.setVariable("motorbikeName", emailSuccessBookingForLessorDto.getMotorbikeName());
            context.setVariable("motorbikePlate", emailSuccessBookingForLessorDto.getMotorbikePlate());
            context.setVariable("bookingTime", emailSuccessBookingForLessorDto.getBookingTime());
            context.setVariable("startDate", emailSuccessBookingForLessorDto.getStartDate());
            context.setVariable("endDate", emailSuccessBookingForLessorDto.getEndDate());
            context.setVariable("receiveLocation", emailSuccessBookingForLessorDto.getReceiveLocation());

            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedTotalPrice = currencyFormatter.format(emailSuccessBookingForLessorDto.getTotalPrice());
            context.setVariable("totalPrice", formattedTotalPrice);
            String formattedDepositPrice = currencyFormatter.format(emailSuccessBookingForLessorDto.getTotalPrice() * 30/ 100);
            context.setVariable("depositPrice", formattedDepositPrice);
            String htmlContent = templateEngine.process("sendEmailDepositMadeBookingForLessor", context);
            mimeMessageHelper.setText(htmlContent, true);


            javaMailSender.send(mimeMessage);
            return "mail send";
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendEmailDepositNotification(EmailSuccessBookingDto emailSuccessBookingDto) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(emailSuccessBookingDto.getRenterEmail());
            mimeMessageHelper.setSubject("Thông Báo Hạn Đặt Cọc");

            Context context = new Context();
            context.setVariable("userName", emailSuccessBookingDto.getRenterName());
            context.setVariable("motorbikeName", emailSuccessBookingDto.getMotorbikeName());
            context.setVariable("motorbikePlate", emailSuccessBookingDto.getMotorbikePlate());
            context.setVariable("bookingTime", emailSuccessBookingDto.getBookingTime());
            context.setVariable("startDate", emailSuccessBookingDto.getStartDate());
            context.setVariable("endDate", emailSuccessBookingDto.getEndDate());
            context.setVariable("receiveLocation", emailSuccessBookingDto.getReceiveLocation());

            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedTotalPrice = currencyFormatter.format(emailSuccessBookingDto.getTotalPrice());
            context.setVariable("totalPrice", formattedTotalPrice);
            String formattedDepositPrice = currencyFormatter.format(emailSuccessBookingDto.getTotalPrice() * 30/ 100);
            context.setVariable("depositPrice", formattedDepositPrice);
            String htmlContent = templateEngine.process("sendEmailDepositNoti", context);
            mimeMessageHelper.setText(htmlContent, true);


            javaMailSender.send(mimeMessage);
            return "mail send";
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendEmailRentingBooking(EmailSuccessBookingDto emailSuccessBookingDto) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(emailSuccessBookingDto.getRenterEmail());
            mimeMessageHelper.setSubject("Giao Xe Thành Công");

            Context context = new Context();
            context.setVariable("userName", emailSuccessBookingDto.getRenterName());
            context.setVariable("motorbikeName", emailSuccessBookingDto.getMotorbikeName());
            context.setVariable("motorbikePlate", emailSuccessBookingDto.getMotorbikePlate());
            context.setVariable("bookingTime", emailSuccessBookingDto.getBookingTime());
            context.setVariable("startDate", emailSuccessBookingDto.getStartDate());
            context.setVariable("endDate", emailSuccessBookingDto.getEndDate());
            context.setVariable("receiveLocation", emailSuccessBookingDto.getReceiveLocation());

            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedTotalPrice = currencyFormatter.format(emailSuccessBookingDto.getTotalPrice());
            context.setVariable("totalPrice", formattedTotalPrice);
            String htmlContent = templateEngine.process("sendEmailRentingBooking", context);
            mimeMessageHelper.setText(htmlContent, true);


            javaMailSender.send(mimeMessage);
            return "mail send";
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendEmailRentingBookingForLessor(EmailSuccessBookingForLessorDto emailSuccessBookingForLessorDto) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(emailSuccessBookingForLessorDto.getLessorEmail());
            mimeMessageHelper.setSubject("Giao Xe Thành Công");

            Context context = new Context();
            context.setVariable("userName", emailSuccessBookingForLessorDto.getLessorName());
            context.setVariable("renterName", emailSuccessBookingForLessorDto.getRenterName());
            context.setVariable("motorbikeName", emailSuccessBookingForLessorDto.getMotorbikeName());
            context.setVariable("motorbikePlate", emailSuccessBookingForLessorDto.getMotorbikePlate());
            context.setVariable("bookingTime", emailSuccessBookingForLessorDto.getBookingTime());
            context.setVariable("startDate", emailSuccessBookingForLessorDto.getStartDate());
            context.setVariable("endDate", emailSuccessBookingForLessorDto.getEndDate());
            context.setVariable("receiveLocation", emailSuccessBookingForLessorDto.getReceiveLocation());

            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedTotalPrice = currencyFormatter.format(emailSuccessBookingForLessorDto.getTotalPrice());
            context.setVariable("totalPrice", formattedTotalPrice);
            String htmlContent = templateEngine.process("sendEmailRentingBookingForLessor", context);
            mimeMessageHelper.setText(htmlContent, true);


            javaMailSender.send(mimeMessage);
            return "mail send";
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendEmailDoneBooking(EmailSuccessBookingDto emailSuccessBookingDto) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(emailSuccessBookingDto.getRenterEmail());
            mimeMessageHelper.setSubject("Hoàn Thành Chuyến");

            Context context = new Context();
            context.setVariable("userName", emailSuccessBookingDto.getRenterName());
            context.setVariable("motorbikeName", emailSuccessBookingDto.getMotorbikeName());
            context.setVariable("motorbikePlate", emailSuccessBookingDto.getMotorbikePlate());
            context.setVariable("bookingTime", emailSuccessBookingDto.getBookingTime());
            context.setVariable("startDate", emailSuccessBookingDto.getStartDate());
            context.setVariable("endDate", emailSuccessBookingDto.getEndDate());
            context.setVariable("receiveLocation", emailSuccessBookingDto.getReceiveLocation());

            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedTotalPrice = currencyFormatter.format(emailSuccessBookingDto.getTotalPrice());
            context.setVariable("totalPrice", formattedTotalPrice);
            String htmlContent = templateEngine.process("sendEmailDoneBooking", context);
            mimeMessageHelper.setText(htmlContent, true);


            javaMailSender.send(mimeMessage);
            return "mail send";
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendEmailDoneBookingForLessor(EmailSuccessBookingForLessorDto emailSuccessBookingForLessorDto) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(emailSuccessBookingForLessorDto.getLessorEmail());
            mimeMessageHelper.setSubject("Hoàn Thành Chuyến");

            Context context = new Context();
            context.setVariable("userName", emailSuccessBookingForLessorDto.getLessorName());
            context.setVariable("renterName", emailSuccessBookingForLessorDto.getRenterName());
            context.setVariable("motorbikeName", emailSuccessBookingForLessorDto.getMotorbikeName());
            context.setVariable("motorbikePlate", emailSuccessBookingForLessorDto.getMotorbikePlate());
            context.setVariable("bookingTime", emailSuccessBookingForLessorDto.getBookingTime());
            context.setVariable("startDate", emailSuccessBookingForLessorDto.getStartDate());
            context.setVariable("endDate", emailSuccessBookingForLessorDto.getEndDate());
            context.setVariable("receiveLocation", emailSuccessBookingForLessorDto.getReceiveLocation());

            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedTotalPrice = currencyFormatter.format(emailSuccessBookingForLessorDto.getTotalPrice());
            context.setVariable("totalPrice", formattedTotalPrice);
            String htmlContent = templateEngine.process("sendEmailDoneBookingForLessor", context);
            mimeMessageHelper.setText(htmlContent, true);


            javaMailSender.send(mimeMessage);
            return "mail send";
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }




}
