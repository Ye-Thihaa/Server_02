package com.example.server.Service;

import java.time.LocalDateTime;

public interface OtpService {
    void deleteOtpByEmail(String email);
    void saveOtp(String email, String code, LocalDateTime expirationTime);
    boolean verifyOtp(String email, String code);
}

