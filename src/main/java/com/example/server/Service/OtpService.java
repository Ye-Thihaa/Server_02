package com.example.server.Service;

import com.example.server.Repository.OtpCodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OtpService {

    private final OtpCodeRepository otpCodeRepository;

    public OtpService(OtpCodeRepository otpCodeRepository) {
        this.otpCodeRepository = otpCodeRepository;
    }

    @Transactional
    public void deleteOtpByEmail(String email) {
        otpCodeRepository.deleteByEmail(email);
    }

    // other methods
}

