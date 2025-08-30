package com.example.server.dto.Request;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@RequiredArgsConstructor
@Getter
@Setter

public class PostRequestDto {
    private String id; // userId
    private Long postId;
    private String content;
    private String imageUrl;
    private String visibility;
    private Long sharedPostId;
    private String postStatus;
    
}
