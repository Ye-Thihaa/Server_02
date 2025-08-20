package com.example.server.dto.Request;

import com.example.server.Model.Reaction;

public class ReactionRequestDto {
    private Long id;
    private Long postId;
    private Long userId;
    private Reaction.ReactionType reaction;
    
    public ReactionRequestDto(Long id,Long postId,Long userId, Reaction.ReactionType reaction) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.reaction = reaction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getPostId() {
        return postId;
    }
    
    public void setPostId(Long postId) {
        this.postId = postId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Reaction.ReactionType getReaction() { return reaction; }

    public void setReaction(Reaction.ReactionType reaction) { this.reaction = reaction; }

}
