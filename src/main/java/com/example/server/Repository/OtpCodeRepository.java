package com.example.server.Repository;

import com.example.server.Model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpCodeRepository extends JpaRepository<Otp, Integer> {
    Optional<Otp> findByEmailAndCode(String email, String code);
    void deleteByEmail(String email); // clean up old OTPs
}

