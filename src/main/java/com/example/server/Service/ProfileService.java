package com.example.server.Service;

import com.example.server.Config.ApiResponse;
import com.example.server.dto.Request.ProfileRequestDto;
import com.example.server.dto.Response.ProfileResponseDto;

public interface ProfileService {
    ApiResponse getUserDetails(final Long userId);
    ApiResponse updateProfileDetails(ProfileRequestDto profileRequestDto);
}
