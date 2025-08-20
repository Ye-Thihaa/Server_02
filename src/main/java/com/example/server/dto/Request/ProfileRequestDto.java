package com.example.server.dto.Request;

public class ProfileRequestDto {
    private Long userId;
    private String bio;
    private String avatarUrl;
    private String location;
    
    public ProfileRequestDto(Long userId, String bio, String avatarUrl, String location) {
        this.userId = userId;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.location = location;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
