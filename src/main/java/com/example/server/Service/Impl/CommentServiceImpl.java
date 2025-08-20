package com.example.server.Service.Impl;

import com.example.server.Config.ApiResponse;
import com.example.server.Model.Comment;
import com.example.server.Model.Post;
import com.example.server.Model.User;
import com.example.server.Repository.CommentRepository;
import com.example.server.Repository.PostRepository;
import com.example.server.Repository.UserRepository;
import com.example.server.Service.CommentService;
import com.example.server.Service.NotificationService;
import com.example.server.dto.Request.CommentRequestDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;
    
    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository, NotificationService notificationService) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.notificationService = notificationService;
    }
    
    @Override
    public ApiResponse makeComment(CommentRequestDto commentRequestDto){
        ZonedDateTime now = ZonedDateTime.now();
        String notification_Type = "comment";

        User user = userRepository.findById(commentRequestDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Post post = postRepository.findById(commentRequestDto.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setCommentText(commentRequestDto.getComment());
        comment.setCreatedAt(now);
        commentRepository.save(comment);

        // notify all users except author
//        List<User> allUsers = userRepository.findAll();
        notificationService.notifyPostOwner(user, post, notification_Type);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("userId", comment.getUser().getId());
        responseData.put("postId", comment.getPost().getId());
        responseData.put("comment", comment);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(responseData)
                .message("Comment to a post")
                .build();
    }
    
    @Override
    public ApiResponse reCreateComment(CommentRequestDto commentRequestDto){
        ZonedDateTime now = ZonedDateTime.now();

        User user = userRepository.findById(commentRequestDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Post post = postRepository.findById(commentRequestDto.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        
        Comment parentComment  = commentRepository.findById(commentRequestDto.getCommentOn())
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setCommentText(commentRequestDto.getComment());
        comment.setCreatedAt(now);
        comment.setCommentOn(parentComment);
        commentRepository.save(comment);

        // notify all users except author
//        List<User> allUsers = userRepository.findAll();
        notificationService.notifyCommentOwner(user, parentComment, post);
        
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("userId", comment.getUser().getId());
        responseData.put("postId", comment.getPost().getId());
        responseData.put("comment", comment);
        responseData.put("commentOn", comment.getCommentOn());
        responseData.put("createdAt", comment.getCreatedAt());

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(responseData)
                .message("Comment to a parent comment")
                .build();
    }
    
    @Override
    public ApiResponse getAllComments() {
        List<Comment> comments = commentRepository.findAll();

        List<Map<String, Object>> commentList = comments.stream().map(comment -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", comment.getId());
            map.put("comment", comment.getCommentText());
            map.put("createdAt", comment.getCreatedAt());
            map.put("userId", comment.getUser().getId());
            map.put("postId", comment.getPost().getId());
            map.put("commentOn", comment.getCommentOn() != null ? comment.getCommentOn().getId() : null);
            return map;
        }).toList();

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(commentList)
                .message("Fetch all comment")
                .build();
    }
    
    @Override
    public ApiResponse deleteComment(final Long userId,final Long commentId) {
        Comment comment = commentRepository.findByUserIdAndId(userId, commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found or does not belong to user"));
        
        commentRepository.delete(comment);
        
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("userId", userId);
        responseData.put("commentId", commentId);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(responseData)
                .message("Delete comment")
                .build();
    }

}
