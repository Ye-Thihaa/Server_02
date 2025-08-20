package com.example.server.Controller;

import com.example.server.Config.ApiResponse;
import com.example.server.Config.util.ResponseUtil;
import com.example.server.Model.Profile;
import com.example.server.Service.ProfileService;
import com.example.server.dto.Request.ProfileRequestDto;
import com.example.server.dto.Response.ProfileResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.connector.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin( origins = "https://orbi-uit.vercel.app")
public class ProfileRestController {
    
    private ProfileService profileService;
    
    public ProfileRestController(ProfileService profileService) {
        this.profileService = profileService;
    }
    
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> showProfileData(@PathVariable("userId") Long userId, HttpServletRequest request) {
        return ResponseUtil.buildResponse(request, profileService.getUserDetails(userId));
    }
    
    @PatchMapping("/user/{userId}/update-info")
    public ResponseEntity<ApiResponse> updateProfileData(@PathVariable("userId") Long userId, @RequestBody ProfileRequestDto profileRequestDto, HttpServletRequest request) {
        profileRequestDto.setUserId(userId);
        return ResponseUtil.buildResponse(request, profileService.updateProfileDetails(profileRequestDto));
    }
}
