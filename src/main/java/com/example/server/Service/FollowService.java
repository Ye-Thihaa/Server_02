package com.example.server.Service;

import com.example.server.Config.ApiResponse;
import com.example.server.dto.Request.FollowRequestDto;

public interface FollowService {
    public ApiResponse makeFollow(FollowRequestDto followRequestDto);
}
