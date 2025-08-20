package com.example.server.dto.Response;

import com.example.server.Model.Comment;
import com.example.server.Model.Post;
import com.example.server.Model.Reaction;

import java.util.List;

// PostResponseDTO.java
public class PostResponseDTO {
    private Long id;
    private String content;
    private List<Reaction> reactions;
    private List<Comment> comments;

    public PostResponseDTO(Post post, List<Reaction> reactions, List<Comment> comments) {
        this.id = post.getId();
        this.content = post.getContent();
        this.reactions = reactions;
        this.comments = comments;
    }

    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
