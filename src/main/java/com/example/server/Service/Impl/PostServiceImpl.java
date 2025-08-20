package com.example.server.Service.Impl;

import com.example.server.Config.ApiResponse;
import com.example.server.Model.*;
import com.example.server.Repository.*;
import com.example.server.Service.NotificationService;
import com.example.server.Service.PostService;
import com.example.server.dto.Request.PostRequestDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ReactionRepository reactionRepository;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository,
                           ReactionRepository reactionRepository,
                           CommentRepository commentRepository,
                           NotificationService notificationService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.reactionRepository = reactionRepository;
        this.commentRepository = commentRepository;
        this.notificationService = notificationService;
    }

    // create post (C)
    @Override
    public ApiResponse createPost(PostRequestDto postRequestDto) {
        ZonedDateTime now = ZonedDateTime.now();

        User user = userRepository.findById(postRequestDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User profile not found"));

        Post post = new Post();
        post.setContent(postRequestDto.getContent());
        post.setCreatedAt(now);
        post.setSharedPost(null);
        post.setVisibility(postRequestDto.getVisibility() != null ? postRequestDto.getVisibility() : "all");
        post.setImageUrl(postRequestDto.getImageUrl() != null ? postRequestDto.getImageUrl() : "");
        post.setUser(user);
        postRepository.save(post);

        // notify all users except author
        List<User> allUsers = userRepository.findAll();
        notificationService.notifyAllUsersExceptAuthor(user, post, allUsers);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("user_id", user.getId());
        responseData.put("content", post.getContent());
        responseData.put("image_url", post.getImageUrl());
        responseData.put("created_at", post.getCreatedAt());
        responseData.put("visibility", post.getVisibility());

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(responseData)
                .message("Post created successfully")
                .build();
    }

    // fetch all posts (R)
    @Override
    public ApiResponse getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<Map<String, Object>> responseDataList = new ArrayList<>();

        for (Post post : posts) {
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("id", post.getId());
            responseData.put("user_id", post.getUser().getId());
            responseData.put("content", post.getContent());
            responseData.put("image_url", post.getImageUrl());
            responseData.put("created_at", post.getCreatedAt());
            responseData.put("visibility", post.getVisibility());
            responseData.put("uploaded_at", post.getUploadedAt());
            responseData.put("shared_post_id", post.getSharedPost());

            // Reactions
            var reactions = reactionRepository.findByPostId(post.getId());
            List<Map<String, Object>> reactionList = new ArrayList<>();
            for (var reaction : reactions) {
                Map<String, Object> r = new HashMap<>();
                r.put("id", reaction.getId());
                r.put("type", reaction.getReactionType());
                r.put("user_id", reaction.getUser().getId());
                reactionList.add(r);
            }
            responseData.put("reactions", reactionList);

            // Comments
            var comments = commentRepository.findByPostId(post.getId());
            List<Map<String, Object>> commentList = new ArrayList<>();
            for (var comment : comments) {
                Map<String, Object> c = new HashMap<>();
                c.put("id", comment.getId());
                c.put("text", comment.getCommentText());
                c.put("user_id", comment.getUser().getId());
                c.put("created_at", comment.getCreatedAt());
                c.put("comment_on", comment.getCommentOn());
                commentList.add(c);
            }
            responseData.put("comments", commentList);

            responseDataList.add(responseData);
        }

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(responseDataList)
                .message("All posts with reactions and comments fetched successfully")
                .build();
    }
    
    // share the post (S)
    @Override
    public ApiResponse sharedPost(PostRequestDto postRequestDto) {
        ZonedDateTime now = ZonedDateTime.now();
        User user = userRepository.findById(postRequestDto.getId()).orElseThrow(() -> new EntityNotFoundException("User profile not found"));
        Post originalPost = postRepository.findById(postRequestDto.getSharedPostId()).orElseThrow(() -> new EntityNotFoundException("Shared post not found"));
        
        Post sharedPost = new Post();
        sharedPost.setUser(user);
        sharedPost.setContent(postRequestDto.getContent());
        sharedPost.setCreatedAt(now);
        sharedPost.setVisibility(postRequestDto.getVisibility() != null ? postRequestDto.getVisibility() : "public");
        sharedPost.setImageUrl(postRequestDto.getImageUrl() != null ? postRequestDto.getImageUrl() : "");
        sharedPost.setSharedPost(originalPost);
        postRepository.save(sharedPost);
        
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("user_id", user.getId());
        responseData.put("content", sharedPost.getContent());
        responseData.put("created_at", sharedPost.getCreatedAt());
        responseData.put("visibility", sharedPost.getVisibility());
        responseData.put("image_url", sharedPost.getImageUrl());
        responseData.put("shared_post_id", sharedPost.getSharedPost());
        
        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(responseData)
                .message("Post sharing is successfully done.")
                .build();
    }

    // update posts (U)
    @Override
    public ApiResponse updatedPosts(PostRequestDto postRequestDto) {
        ZonedDateTime now = ZonedDateTime.now();

        Post post = postRepository.findByIdAndUserId(postRequestDto.getPostId(), postRequestDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("You are not allowed to update this post!"));

        post.setContent(postRequestDto.getContent());
        post.setVisibility(
                postRequestDto.getVisibility() != null
                        ? postRequestDto.getVisibility()
                        : post.getVisibility()
        );
        post.setImageUrl(postRequestDto.getImageUrl());
        post.setUploadedAt(now);

        postRepository.save(post);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(post)
                .message("Post updated successfully!")
                .build();
    }

    // delete posts (D)
    @Override
    public ApiResponse deletePosts(final Long userId, final Long postId) {
        Post post = postRepository.findByIdAndUserId(postId, userId).orElse(null);
        if (post == null){
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.CREATED.value())
                    .data("null")
                    .message("User not found (or) You are not allowed to delete this post.")
                    .build();
        }
        
        // Finally delete the post
        postRepository.delete(post);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("userId", userId);
        responseData.put("postId", postId);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(responseData)
                .message("Delete post successfully")
                .build();
    }
    
}
