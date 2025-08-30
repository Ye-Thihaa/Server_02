package com.example.server.dto.Request;

import com.example.server.Model.Reaction;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Data
@RequiredArgsConstructor
@Getter
@Setter
public class ReactionRequestDto {
    private Long id;
    private Long postId;
    private String userId;
    private Reaction.ReactionType reaction;
    
}
