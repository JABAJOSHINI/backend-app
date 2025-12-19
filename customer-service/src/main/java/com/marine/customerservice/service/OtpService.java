package com.marine.customerservice.service;

import com.marine.customerservice.Entity.OtpEntity;
import com.marine.customerservice.Repository.OtpRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    // Generate 6-digit OTP
    public String generateOtp() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }


    public void saveOtp(String email, String otp) {
        LocalDateTime expiryTime = LocalDateTime.now().plusSeconds(60);
        // Check if OTP already exists for this email
        Optional<OtpEntity> existingOtp = otpRepository.findByEmail(email);
        if (existingOtp.isPresent()) {
            otpRepository.delete(existingOtp.get()); // delete existing record
        }
        OtpEntity entity = new OtpEntity(email, otp, expiryTime);
        otpRepository.save(entity);
    }

    // Verify OTP
    @Transactional
    public boolean verifyOtp(String email, String otp) {
        Optional<OtpEntity> otpData = otpRepository.findByEmail(email);
        if (otpData.isPresent()) {
            OtpEntity entity = otpData.get();
            if (entity.getOtp().equals(otp)) {
                // Check expiry
                if (entity.getExpiryTime().isAfter(LocalDateTime.now())) {
                    otpRepository.deleteByEmail(email); // cleanup
                    return true; // valid OTP
                } else {
                    return false; // expired OTP
                }
            }
            return false; // invalid OTP

        }
        return false;
    }
}
