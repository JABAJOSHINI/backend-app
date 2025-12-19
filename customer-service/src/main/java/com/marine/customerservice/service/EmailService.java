package com.marine.customerservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

    @Service
    public class EmailService {

        @Autowired
        private JavaMailSender mailSender;

        public void sendOtpEmail(String toEmail, String otp) {
            String subject = "🔐 Your OTP Verification Code";
            String content = buildOtpEmailTemplate(otp);

            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setTo(toEmail);
                helper.setSubject(subject);
                helper.setText(content, true); // true = HTML

                mailSender.send(message);
                System.out.println("✅ OTP email sent successfully to " + toEmail);
            } catch (MessagingException e) {
                System.err.println("❌ Failed to send OTP email: " + e.getMessage());
            }
        }

        // Build HTML email template
        private String buildOtpEmailTemplate(String otp) {
            return """
        <html>
        <body style="font-family: Arial, sans-serif; background-color:#f7f7f7; padding:20px;">
          <div style="max-width:600px; margin:auto; background-color:white; border-radius:8px; box-shadow:0 2px 8px rgba(0,0,0,0.1); padding:20px;">
            <h2 style="color:#007bff; text-align:center;">KauferKart</h2>
            <hr style="border:0; height:1px; background:#ddd;">
            <p style="font-size:16px;">Hello,</p>
            <p style="font-size:16px;">Your One-Time Password (OTP) for verification is:</p>
            
            <div style="text-align:center; margin:30px 0;">
              <span style="font-size:28px; font-weight:bold; letter-spacing:4px; color:#007bff;">%s</span>
            </div>

            <p style="font-size:15px;">This OTP is valid for <b>60 seconds</b>. Please do not share it with anyone.</p>
            <p style="font-size:15px;">If you did not request this, please ignore this email.</p>
            
            <br/>
            <p style="font-size:14px; color:#555;">Best regards,<br/><b>KauferKart</b></p>
          </div>
        </body>
        </html>
        """.formatted(otp);
        }
    }
