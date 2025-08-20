package com.example.server.Controller;

import com.example.server.Model.Otp;
import com.example.server.Model.User;
import com.example.server.Repository.OtpCodeRepository;
import com.example.server.Service.EmailService;
import com.example.server.Service.OtpService;
import com.example.server.Service.UserService;
import com.example.server.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://orbi-uit.vercel.app")
public class UserRestController {
    
    private final UserService userService;
    private final EmailService emailService;
    private final OtpCodeRepository otpCodeRepository;
    private final OtpService otpService;
    
    public UserRestController(UserService userService, EmailService emailService, OtpCodeRepository otpCodeRepository, OtpService otpService) {
        this.userService = userService;
        this.emailService = emailService;
        this.otpCodeRepository = otpCodeRepository;
        this.otpService = otpService;
    }

//    getting all User from db
    @GetMapping
    public List<User> getUsers() {
        System.out.println(userService.getAllUsers());
        return userService.getAllUsers();
    }

    @GetMapping("/send_id")
    public Long getUser(@RequestParam("email") String email) {
        if (email != null && !email.isEmpty()) {
            return userService.responseId(email);
        }
        return 0L;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserDTO userDTO) {
        String result = userService.registerUser(userDTO);

        if (result.equals("Signup successful")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        boolean result = userService.loginUser(userDTO);

        if (result) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }
    
//    @GetMapping("/signup")
//    public ResponseEntity<String> signupGet() {
//        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
//                .body("GET method not allowed. Please use POST.");
//    }

    @PostMapping("/get-email")
    public ResponseEntity<?> checkUserExists(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        Optional<User> user = userService.searchUser(email);
        Map<String, Object> response = new HashMap<>();

        if (user.isEmpty()) {
            response.put("exists", false);
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        // Clean up any old OTPs
        otpService.deleteOtpByEmail(email);

        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Expiration time = now + 4 minutes
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(4);

        // Save to DB
        Otp otpCode = new Otp();
        otpCode.setEmail(email);
        otpCode.setCode(otp);
        otpCode.setExpirationTime(expirationTime);
        otpCodeRepository.save(otpCode);

        // Send Email
        emailService.sendOtp(email, otp);

        response.put("exists", true);
        response.put("message", "OTP sent to your email");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("otp");

        Optional<Otp> otpRecord = otpCodeRepository.findByEmailAndCode(email, code);
        Map<String, Object> response = new HashMap<>();

        if (otpRecord.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
        }

        if (otpRecord.get().getExpirationTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.GONE).body("OTP expired");
        }

        response.put("exists", true);
        response.put("message", "OTP verified");
        // OTP is valid
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email       = request.get("email");
        String newPassword = request.get("password");

        try {
            userService.resetPassword(email, newPassword);
            return ResponseEntity.ok("Password has been reset successfully.");
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
