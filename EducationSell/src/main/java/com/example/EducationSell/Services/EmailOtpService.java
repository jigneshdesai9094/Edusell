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
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailOtpService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mailUsername;

    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();
    private final Map<String,UserRegisterDTO> pendingRegistraction= new ConcurrentHashMap<>();

    public Long generateOtp()
    {
        System.out.println(Math.random()*1000000);
        Long otp = (long) Math.ceil(Math.random() * 1000000);
        System.out.println("otp "+otp);
        return otp;
    }

    public void sendOtp(UserRegisterDTO dto)
    {
        Long otp = generateOtp();

        pendingRegistraction.put(dto.getEmail(),dto);

        otpStore.put(dto.getEmail(),new OtpData(otp, LocalDateTime.now().plusMinutes(5)));
        System.out.println("otp store ------------------ "+otp);
        System.out.println(otpStore);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailUsername); // Replace with your email

        message.setTo(dto.getEmail());
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp + "\nThis code is valid for 5 minutes.");
        javaMailSender.send(message);

    }

    public UserRegisterDTO verifyOtp(VerificationDTO verificationDTO) throws Exception {
        System.out.println("in verifyotp show otpstore : ---------");
        System.out.println(otpStore);
        OtpData otpData = otpStore.get(verificationDTO.getEmail());

        if(otpData == null)
        {
            throw  new Exception("please, try again");
        }

        else if(Objects.equals(otpData.getOtp(), verificationDTO.getOtp()) && LocalDateTime.now().isBefore(otpData.getExpiryTime()))
        {
            System.out.println("otp and time is match");
            System.out.println(pendingRegistraction);
            System.out.println(pendingRegistraction.get(verificationDTO.getEmail()));
            return  pendingRegistraction.get(verificationDTO.getEmail());
        }
        if(LocalDateTime.now().isAfter(otpData.getExpiryTime()))
            throw new Exception("otp time expire , please regenrate otp");
        throw   new Exception("otp not match , please enter correct otp");
    }
}
