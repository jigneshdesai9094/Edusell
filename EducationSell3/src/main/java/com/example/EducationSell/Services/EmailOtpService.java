package com.example.EducationSell.Services;

import com.example.EducationSell.DTO.UserRegisterDTO;
import com.example.EducationSell.DTO.VerificationDTO;
import com.example.EducationSell.Helper.OtpData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailOtpService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mailUsername;

    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();
    private final Map<String, UserRegisterDTO> pendingRegistration = new ConcurrentHashMap<>();

    public Long generateOtp() {
        Long otp = (long) Math.ceil(Math.random() * 1000000);
        return otp;
    }

    public void sendOtp(UserRegisterDTO dto) {
        if (dto == null || dto.getEmail() == null) {
            throw new IllegalArgumentException("Invalid user registration details");
        }

        Long otp = generateOtp();
        pendingRegistration.put(dto.getEmail(), dto);
        otpStore.put(dto.getEmail(), new OtpData(otp, LocalDateTime.now().plusMinutes(5)));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailUsername);
        message.setTo(dto.getEmail());
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp + "\nThis code is valid for 5 minutes.");
        javaMailSender.send(message);
    }

    public UserRegisterDTO verifyOtp(VerificationDTO verificationDTO) {
        if (verificationDTO == null || verificationDTO.getEmail() == null || verificationDTO.getOtp() == null) {
            throw new IllegalArgumentException("Invalid verification details");
        }

        OtpData otpData = otpStore.get(verificationDTO.getEmail());
        if (otpData == null) {
            throw new IllegalArgumentException("No OTP found for email: " + verificationDTO.getEmail());
        }

        if (!otpData.getOtp().equals(verificationDTO.getOtp())) {
            throw new IllegalArgumentException("OTP does not match");
        }

        if (LocalDateTime.now().isAfter(otpData.getExpiryTime())) {
            throw new IllegalArgumentException("OTP has expired, please regenerate OTP");
        }

        UserRegisterDTO dto = pendingRegistration.get(verificationDTO.getEmail());
        if (dto == null) {
            throw new IllegalArgumentException("No pending registration found for email: " + verificationDTO.getEmail());
        }

        return dto;
    }
}