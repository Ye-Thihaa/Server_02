package com.example.server.Service;

import com.example.server.Config.ApiResponse;
import com.example.server.dto.Request.PostRequestDto;

public interface PostService {
    ApiResponse createPost(PostRequestDto postRequestDto);
    ApiResponse getAllPosts(); // with reactions & comments
    ApiResponse updatedPosts(PostRequestDto postRequestDto);
    ApiResponse sharedPost(PostRequestDto postRequestDto);
    ApiResponse deletePosts(final String userId, final Long postId);
    ApiResponse fetchPostTag(final String post_type);
}
