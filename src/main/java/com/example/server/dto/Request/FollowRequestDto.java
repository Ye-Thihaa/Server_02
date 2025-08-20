package com.example.server.dto.Request;

public class FollowRequestDto {
    private Long requestUserId;
    private Long responseUserId;
    
    public FollowRequestDto(Long requestUserId, Long responseUserId) {
        this.requestUserId = requestUserId;
        this.responseUserId = responseUserId;
    }

    public Long getRequestUserId() {
        return requestUserId;
    }

    public void setRequestUserId(Long requestUserId) {
        this.requestUserId = requestUserId;
    }

    public Long getResponseUserId() {
        return responseUserId;
    }

    public void setResponseUserId(Long responseUserId) {
        this.responseUserId = responseUserId;
    }
}
