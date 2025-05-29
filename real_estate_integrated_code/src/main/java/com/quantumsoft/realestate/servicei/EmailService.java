package com.quantumsoft.realestate.servicei;

public interface EmailService {
    //void sendSimpleMessage(String to, String subject, String text);
    void sendVerificationEmail(String email, String verificationToken);
    void sendResetPasswordEmail(String email, String resetToken);

    void sendSimpleMessage(String email, String verifyYourEmail, String s);
}
