package com.example.server.Service.Impl;

import com.example.server.Config.ApiResponse;
import com.example.server.Config.exceptions.EntityNotFoundException;
import com.example.server.Model.Follow;
import com.example.server.Model.User;
import com.example.server.Repository.FollowRepository;
import com.example.server.Repository.UserRepository;
import com.example.server.Service.FollowService;
import com.example.server.dto.Request.FollowRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class FollowServiceImpl implements FollowService {
    
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowServiceImpl(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ApiResponse makeFollow(FollowRequestDto followRequestDto) {
        ZonedDateTime now = ZonedDateTime.now();

        User follower = userRepository.findById(followRequestDto.getRequestUserId())
                .orElseThrow(() -> new EntityNotFoundException("Follower not found")); 

        User followee = userRepository.findById(followRequestDto.getResponseUserId())
                .orElseThrow(() -> new EntityNotFoundException("Followee not found"));

        // Check if already following
        boolean alreadyFollowing = followRepository.existsByFollowerIdAndFolloweeId(follower.getId(), followee.getId());
        if (alreadyFollowing) {
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.CONFLICT.value())
                    .data(null)
                    .message("Already following this user!")
                    .build();
        }

        // Create follow relationship
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowee(followee);
        follow.setCreatedAt(now);

        followRepository.save(follow);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("follower_id", follower.getId());
        responseData.put("followee_id", followee.getId());
        responseData.put("created_at", follow.getCreatedAt()); 

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(responseData)
                .message("Followed successfully!")
                .build();
    }

}
