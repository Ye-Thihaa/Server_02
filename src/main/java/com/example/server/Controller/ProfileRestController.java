package com.example.server.Controller;

import com.example.server.Config.ApiResponse;
import com.example.server.Config.util.ResponseUtil;
import com.example.server.Model.Profile;
import com.example.server.Service.ProfileService;
import com.example.server.dto.Request.ProfileRequestDto;
import com.example.server.dto.Response.ProfileResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
@CrossOrigin( origins = "https://orbi-uit.vercel.app")
public class ProfileRestController {
    
    private final ProfileService profileService;
    
//    @GetMapping("/admin/profile-data")
    
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> showProfileData(@PathVariable("userId") String userId, HttpServletRequest request) {
        return ResponseUtil.buildResponse(request, profileService.getUserDetails(userId));
    }
    
    @PatchMapping("/user/{userId}/update-info")
    public ResponseEntity<ApiResponse> updateProfileData(@PathVariable("userId") String userId, @RequestBody ProfileRequestDto profileRequestDto, HttpServletRequest request) {
        profileRequestDto.setUserId(userId);
        return ResponseUtil.buildResponse(request, profileService.uploadProfileDetails(profileRequestDto));
    }
}
