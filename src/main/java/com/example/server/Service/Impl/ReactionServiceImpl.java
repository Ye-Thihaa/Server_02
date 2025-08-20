package com.example.server.Service.Impl;

import com.example.server.Config.ApiResponse;
import com.example.server.Model.Post;
import com.example.server.Model.Reaction;
import com.example.server.Model.User;
import com.example.server.Repository.PostRepository;
import com.example.server.Repository.ReactionRepository;
import com.example.server.Repository.UserRepository;
import com.example.server.Service.NotificationService;
import com.example.server.Service.ReactionService;
import com.example.server.dto.Request.ReactionRequestDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@Transactional
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;

    public ReactionServiceImpl(ReactionRepository reactionRepository, UserRepository userRepository, PostRepository postRepository, NotificationService notificationService) {
        this.reactionRepository = reactionRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.notificationService = notificationService;
    }

    // give reaction to post (C)
    @Override
    public ApiResponse setReaction(ReactionRequestDto reactionRequestDto) {
        ZonedDateTime now = ZonedDateTime.now();
        String notificationType = "reaction";
        
        User user = userRepository.findById(reactionRequestDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Post post = postRepository.findById(reactionRequestDto.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        Reaction reaction = new Reaction();
        reaction.setUser(user);
        reaction.setPost(post);
        reaction.setReactionType(reactionRequestDto.getReaction());
        reaction.setCreatedAt(now);
        reactionRepository.save(reaction);

        notificationService.notifyPostOwner(user, post, notificationType);
        
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("post_id", reaction.getPost().getId());
        responseData.put("user_id", reaction.getUser().getId());
        responseData.put("reaction_type", reaction.getReactionType());
        responseData.put("created_at", reaction.getCreatedAt());

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(responseData)
                .message("React to a post")
                .build();
    }

    // get all reactions (R)
    @Override
    public ApiResponse getAllReaction() {
        List<Reaction> reactions = reactionRepository.findAll();
        List<Map<String, Object>> responseDataList = new ArrayList<>();

        for (Reaction reaction : reactions) {
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("post_id", reaction.getPost().getId());
            responseData.put("user_id", reaction.getUser().getId());
            responseData.put("reaction_type", reaction.getReactionType());
            responseData.put("created_at", reaction.getCreatedAt());
            responseDataList.add(responseData);
        }

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(responseDataList)
                .message("All reactions data fetched successfully")
                .build();
    }

    // delete reaction (D)
    @Override
    public ApiResponse deleteReaction(final Long postId, final Long userId) {
        Reaction checkReaction = reactionRepository.findByPostIdAndUserId(postId, userId)
                .orElse(null);
        if (checkReaction == null) {
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.NO_CONTENT.value())
                    .data(null)
                    .message("Reaction not found")
                    .build();
        }
        
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("post_id", postId);
        responseData.put("user_id", userId);
        responseData.put("reaction_type", checkReaction.getReactionType());
        responseData.put("created_at", checkReaction.getCreatedAt());
        
        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(responseData)
                .message("Delete reaction successfully done.")
                .build();
    }
}