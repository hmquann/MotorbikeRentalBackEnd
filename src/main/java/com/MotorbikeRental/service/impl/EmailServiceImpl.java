package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.service.EmailService;
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

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;


    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
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


}
