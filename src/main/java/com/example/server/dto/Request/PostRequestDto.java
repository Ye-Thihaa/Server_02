package com.example.server.dto.Request;

import com.example.server.Model.Post;

public class PostRequestDto {
    private Long id;
    private long postId;
    private String content;
    private String imageUrl;
    private String visibility;
    private Long sharedPostId;
    
    public PostRequestDto(Long id, String content, String imageUrl, String visibility, Long sharedPostId) {
        this.id = id;
        this.content = content;
        this.imageUrl = imageUrl;
        this.visibility = visibility;
        this.sharedPostId = sharedPostId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public long getPostId() {
        return postId;
    }
    
    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Long getSharedPostId() {
        return sharedPostId;
    }

    public void setSharedPostId(Long sharedPostId) {
        this.sharedPostId = sharedPostId;
    }
}
