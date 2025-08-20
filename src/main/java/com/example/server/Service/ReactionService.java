package com.example.server.Service;

import com.ctc.wstx.shaded.msv_core.datatype.xsd.FinalComponent;
import com.example.server.Config.ApiResponse;
import com.example.server.dto.Request.ReactionRequestDto;

public interface ReactionService {
    ApiResponse setReaction(ReactionRequestDto reactionRequestDto);
    ApiResponse getAllReaction();
    ApiResponse deleteReaction(final Long postId, final Long userId);
}
