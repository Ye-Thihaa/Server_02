package com.example.server.Controller;

import com.example.server.Config.ApiResponse;
import com.example.server.Repository.OtpCodeRepository;
import com.example.server.Service.EmailService;
import com.example.server.Service.OtpService;
import com.example.server.Service.UserService;
import com.example.server.dto.Request.UserLoginRequestDto;
import com.example.server.dto.Request.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "https://orbi-uit.vercel.app")
public class UserRestController {
    
    private final UserService userService;
    private final EmailService emailService;
    private final OtpService otpService;

    @GetMapping("/admin/all")
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<Map<String,Object>> data = userService.getAllUsersWithStudentId();
        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(200)
                .data(data)
                .message("All users fetched successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/signup")
    public String signUp(@RequestBody UserRequestDto userRequestDto) {
        return userService.signUp(userRequestDto);
    }

    // 
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        ApiResponse response = userService.login(userLoginRequestDto);

        // Use the success field or code to decide HTTP status
        if (response.getSuccess() == 1) {
            return ResponseEntity.ok(response); // 200 OK
        } else if (response.getCode() == 401) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else if (response.getCode() == 400) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Request OTP for forget password
    @PostMapping("/forget-password")
    public ResponseEntity<?> forgetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, Object> response = new HashMap<>();

        // Check user exist in Supabase
        if (!userService.checkUserExists(email)) {
            response.put("exists", false);
            response.put("message", "User not found");
            return ResponseEntity.ok(response);
        }

        // Delete old OTPs
        otpService.deleteOtpByEmail(email);

        // Generate OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(10);

        // Save OTP
        otpService.saveOtp(email, otp, expiration);

        // Send OTP via email
        emailService.sendOtp(email, otp);

        response.put("exists", true);
        response.put("message", "OTP sent to your email");
        return ResponseEntity.ok(response);
    }

    // Verify OTP & Reset Password
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        String newPassword = request.get("newPassword");

        // Verify OTP
        if (!otpService.verifyOtp(email, otp)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Invalid or expired OTP"));
        }

        // Update password in Supabase
        if (!userService.updatePassword(email, newPassword)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to reset password"));
        }

        return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
    }
    
}
