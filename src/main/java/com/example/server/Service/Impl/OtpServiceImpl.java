package com.example.server.Service.Impl;

import com.example.server.Model.Otp;
import com.example.server.Repository.OtpCodeRepository;
import com.example.server.Service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpCodeRepository otpRepository;

    @Override
    public void deleteOtpByEmail(String email) {
        otpRepository.deleteByEmail(email);
    }

    @Override
    public void saveOtp(String email, String code, LocalDateTime expirationTime) {
        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setCode(code);
        otp.setExpirationTime(expirationTime);
        otpRepository.save(otp);
    }

    @Override
    public boolean verifyOtp(String email, String code) {
        Optional<Otp> otpOpt = otpRepository.findByEmailAndCode(email, code);
        if (otpOpt.isEmpty()) return false;

        // Expiration check
        if (otpOpt.get().getExpirationTime().isBefore(LocalDateTime.now())) {
            otpRepository.delete(otpOpt.get());
            return false;
        }

        // Valid OTP → delete after use
        otpRepository.delete(otpOpt.get());
        return true;
    }
}
