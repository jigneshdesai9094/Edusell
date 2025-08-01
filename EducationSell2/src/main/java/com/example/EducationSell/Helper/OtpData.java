package com.example.EducationSell.Helper;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OtpData {
    private Long otp;
    private LocalDateTime expiryTime;
}
