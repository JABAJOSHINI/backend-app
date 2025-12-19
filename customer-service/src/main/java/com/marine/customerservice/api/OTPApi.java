package com.marine.customerservice.api;

import com.marine.customerservice.Entity.OtpEntity;
import com.marine.customerservice.Model.OtpVerificationRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/otp")
public interface OTPApi {

    @PostMapping(value = "/saveOtp", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> saveOtp(@RequestBody OtpEntity otpEntity);
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationRequest request);

}
