package com.example.server.dto.Request;

public class CommentRequestDto {
    private String comment;
    private Long userId;
    private Long postId;
    private Long commentOn;
    
    public CommentRequestDto(String comment, Long userId, Long postId) {
        this.comment = comment;
        this.userId = userId;
        this.postId = postId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
    
    public Long getCommentOn() {
        return commentOn;
    }
    
    public void setCommentOn(Long commentOn) {
        this.commentOn = commentOn;
    }
}
