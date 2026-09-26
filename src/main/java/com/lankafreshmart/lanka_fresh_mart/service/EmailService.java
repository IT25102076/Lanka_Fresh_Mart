package com.lankafreshmart.lanka_fresh_mart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetOtp(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("fixhub.service.lk@gmail.com");
            message.setTo(toEmail);
            message.setSubject("Password Reset OTP - Lanka Fresh Mart");
            message.setText("Your OTP for password reset is: " + otp + "\n\nThis OTP will expire in 5 minutes. If you did not request a password reset, please ignore this email.");
            
            // Actually send the email
            mailSender.send(message);
            
            // Also log it for development convenience
            log.info("==================================================");
            log.info("EMAIL MOCK (Since SMTP is not configured yet)");
            log.info("To: {}", toEmail);
            log.info("Subject: Password Reset OTP - Lanka Fresh Mart");
            log.info("OTP Code: {}", otp);
            log.info("==================================================");
            
        } catch (Exception e) {
            log.error("Failed to send OTP email to {}", toEmail, e);
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    public void sendPasswordChangeSuccessEmail(String toEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("fixhub.service.lk@gmail.com");
            message.setTo(toEmail);
            message.setSubject("Password Changed Successfully - Lanka Fresh Mart");
            message.setText("Hello,\n\nYour Lanka Fresh Mart account password has been successfully changed.\n\nIf you did not make this change, please contact our support team immediately.");
            mailSender.send(message);
            log.info("Sent password change success email to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send password change success email to {}", toEmail, e);
            // We don't throw an exception here because the password has already been changed successfully.
            // We don't want to fail the user's request just because the notification email failed.
        }
    }
}
