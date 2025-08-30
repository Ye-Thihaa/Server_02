package com.example.server.dto.Response;

import com.example.server.Model.Comment;
import com.example.server.Model.Post;
import com.example.server.Model.Reaction;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class PostResponseDTO {
    private Long id;
    private String content;
    private List<Reaction> reactions;
    private List<Comment> comments;
}
