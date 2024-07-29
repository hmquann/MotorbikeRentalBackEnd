package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.dto.EmailSuccessBookingDto;
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


}
