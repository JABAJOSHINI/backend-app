package com.marine.customerservice.controller;

import com.example.appcommons.utility.Response;
import com.marine.customerservice.Entity.OtpEntity;
import com.marine.customerservice.Model.OtpVerificationRequest;
import com.marine.customerservice.api.OTPApi;
import com.marine.customerservice.service.EmailService;
import com.marine.customerservice.service.OtpService;
import com.marine.customerservice.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class OTPApiController implements OTPApi {

    private final OtpService otpService;
    private final EmailService emailService;

    @Override
    public ResponseEntity<?> saveOtp(OtpEntity otpEntity) {
        String otp = otpService.generateOtp();
        otpService.saveOtp(otpEntity.getEmail(), otp);
        emailService.sendOtpEmail(otpEntity.getEmail(), otp);
        return ResponseEntity.ok(Map.of("message", "OTP sent successfully"));
    }


    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationRequest request) {
        boolean isValid = otpService.verifyOtp(request.getEmail(), request.getOtp());
        if (isValid) {
         //   userInfoService.activateUser(request.getEmail()); // mark user active
            return ResponseEntity.ok(Map.of("message", "OTP verified successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Invalid or expired OTP"));
        }
    }

}
