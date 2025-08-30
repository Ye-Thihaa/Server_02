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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    
    private ProfileResponseDto convertToDto(User user, Profile profile) {
        ProfileResponseDto dto = new ProfileResponseDto();
        dto.setProfileId(profile.getId());
        dto.setUserId(user.getId());
        dto.setName(user.getUsername());
        dto.setStudentId(user.getStudentId());
        dto.setBio(profile.getBio());
        dto.setAvatarUrl(profile.getAvatarUrl());
        dto.setLocation(profile.getLocation());
        dto.setBatch(profile.getBatch());
        dto.setBirthDate(profile.getBirthDate());
        dto.setCoverUrl(profile.getCoverUrl());
        dto.setYear(profile.getYear());
        dto.setPrivate(profile.getIsPrivate());
        dto.setPhone(profile.getPhone());
    
        return dto;
    }

    
    @Override
    @Transactional(readOnly = true)
    public ApiResponse getUserDetails(final String userId) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException("User Not Found, Please signup your account first"));

        Optional<Profile> profileOpt = profileRepository.findByUser(user);
        Profile profile = profileOpt.orElse(new Profile());
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
    public ApiResponse uploadProfileDetails(ProfileRequestDto profileRequestDto) {
        ZonedDateTime now = ZonedDateTime.now();
        User user = userRepository.findById(UUID.fromString(profileRequestDto.getUserId()))
                .orElseThrow(() -> new EntityNotFoundException("User Not Found, Please signup your account first"));
        
        Profile profileData = new Profile();
        profileData.setUser(user);
        profileData.setBio(profileRequestDto.getBio());
        profileData.setAvatarUrl(profileRequestDto.getAvatarUrl());
        profileData.setLocation(profileRequestDto.getLocation());
        profileData.setBatch(profileRequestDto.getBatch());
        profileData.setBirthDate(profileRequestDto.getBirthDate());
        profileData.setCoverUrl(profileRequestDto.getCoverUrl());
        profileData.setYear(profileRequestDto.getYear());
        profileData.setIsPrivate(profileRequestDto.isPrivate());
        profileData.setUpdatedAt(now.toOffsetDateTime());
        
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
