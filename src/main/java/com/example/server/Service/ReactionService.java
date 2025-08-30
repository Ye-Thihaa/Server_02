package com.example.server.Service;

import com.example.server.Config.ApiResponse;
import com.example.server.dto.Request.ReactionRequestDto;

import java.util.UUID;

public interface ReactionService {
    ApiResponse setReaction(ReactionRequestDto reactionRequestDto);
    ApiResponse getAllReaction();
    ApiResponse deleteReaction(final Long postId, final String userId);
}
