package com.example.server.Service;

import com.example.server.Config.ApiResponse;
import com.example.server.dto.Request.ProfileRequestDto;
import com.example.server.dto.Response.ProfileResponseDto;

import java.util.UUID;

public interface ProfileService {
    ApiResponse getUserDetails(final String userId);
    ApiResponse uploadProfileDetails(ProfileRequestDto profileRequestDto);
}
