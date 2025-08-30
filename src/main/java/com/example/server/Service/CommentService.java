package com.example.server.Service;

import com.example.server.Config.ApiResponse;
import com.example.server.dto.Request.CommentRequestDto;

public interface CommentService {
    ApiResponse makeComment(CommentRequestDto commentRequestDto);
    ApiResponse reCreateComment(CommentRequestDto commentRequestDto);
    ApiResponse deleteComment(final String userId, final Long commentId);
    ApiResponse getAllComments();
}
