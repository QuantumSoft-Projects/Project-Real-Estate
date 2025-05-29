package com.quantumsoft.realestate.serviceimpl;


import com.quantumsoft.realestate.servicei.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendVerificationEmail(String email, String verificationToken) {
        String subject = "Verify your email";
        String verificationLink = "http://localhost:8080/api/users" + "/verify?token=" + verificationToken;
        String message = "Click the following link to verify your email: " + verificationLink;

        sendSimpleMessage(email, subject, message);
    }

    @Override
    public void sendResetPasswordEmail(String email, String resetToken) {

        String subject = "Reset your password";
        String resetLink = "http://localhost:8080/api/users" + "/reset-password?token=" + resetToken;
        String message = "Click the following link to reset your password: " + resetLink;

        sendSimpleMessage(email, subject, message);

    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(to);
                message.setSubject(subject);
                message.setText(text);
                javaMailSender.send(message);
            } catch (Exception e) {
                e.printStackTrace();  // Log the error or handle as needed
                throw new RuntimeException("Failed to send email: " + e.getMessage());
            }
        }
}
