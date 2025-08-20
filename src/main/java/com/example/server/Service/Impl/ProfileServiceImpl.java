package com.example.server.Service.Impl;

import com.example.server.Config.ApiResponse;
import com.example.server.Config.exceptions.EntityNotFoundException;
import com.example.server.Model.Profile;
import com.example.server.Model.User;
import com.example.server.Repository.ProfileRepository;
import com.example.server.Repository.UserRepository;
import com.example.server.Service.ProfileService;
import com.example.server.dto.Request.ProfileRequestDto;
import com.example.server.dto.Response.ProfileResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {
    
    private ProfileRepository profileRepository;
    private UserRepository userRepository;
    
    public ProfileServiceImpl(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    private ProfileResponseDto convertToDto(User user, Profile profile) {
        ProfileResponseDto dto = new ProfileResponseDto();
        dto.setProfileId(profile.getId());
        dto.setUserId(user.getId());
        dto.setName(user.getName());
        dto.setStudentId(user.getStudentId());
        dto.setEmail(user.getEmail());
        dto.setBio(profile.getBio());
        dto.setAvatarUrl(profile.getAvatarUrl());
        dto.setLocation(profile.getLocation());
        dto.setAdmin(profile.getAdmin());
        dto.setBanned(profile.getBanned());
        return dto;
    }
    
    @Override
    public ApiResponse getUserDetails(final Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User Not Found, Please signup your account first"));

        Optional<Profile> porfileOpt = profileRepository.findByUser(user);
        Profile profile = porfileOpt.orElse(new Profile());
        profile.setUser(user);

        ProfileResponseDto responseDto = convertToDto(user, profile);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("profile", responseDto);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(responseData)
                .message("User profile fetched successfully")
                .build();
    }
    
    @Override
    public ApiResponse updateProfileDetails(ProfileRequestDto profileRequestDto) {
        ZonedDateTime now = ZonedDateTime.now();
        User user = userRepository.findById(profileRequestDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User Not Found, Please signup your account first"));
        
        Profile profileData = new Profile();
        profileData.setUser(user);
        profileData.setBio(profileRequestDto.getBio());
        profileData.setAvatarUrl(profileRequestDto.getAvatarUrl());
        profileData.setLocation(profileRequestDto.getLocation());
        profileData.setUpdatedAt(now);
        
        profileRepository.save(profileData);
        
        ProfileResponseDto responseDto = convertToDto(user, profileData);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("profile", responseDto);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(responseData)
                .message("User profile updated successfully")
                .build();
        
    }
}
