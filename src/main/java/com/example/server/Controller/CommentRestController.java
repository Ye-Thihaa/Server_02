package com.example.server.Controller;

import com.example.server.Config.ApiResponse;
import com.example.server.Config.util.ResponseUtil;
import com.example.server.Service.CommentService;
import com.example.server.dto.Request.CommentRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comments")
@CrossOrigin(origins = "https://orbi-uit.vercel.app")
public class CommentRestController {

    private final CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PatchMapping("/user/{userId}/post/{postId}")
    public ResponseEntity<ApiResponse> createComment(@PathVariable("userId") String userId, @PathVariable("postId") Long postId, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
        commentRequestDto.setUserId(userId);
        commentRequestDto.setPostId(postId);
        return ResponseUtil.buildResponse(request, commentService.makeComment(commentRequestDto));
    }

    @PutMapping("/user/{userId}/post/{postId}/{commentId}")
    public ResponseEntity<ApiResponse> reComment(@PathVariable("userId") String userId, @PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
        commentRequestDto.setUserId(userId);
        commentRequestDto.setPostId(postId);
        commentRequestDto.setCommentOn(commentId);
        return ResponseUtil.buildResponse(request, commentService.reCreateComment(commentRequestDto));
    }

    @DeleteMapping("/user/{userId}/delete/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable("userId") String userId, @PathVariable("commentId") Long commentId, HttpServletRequest request) {
        return ResponseUtil.buildResponse(request, commentService.deleteComment(userId, commentId));
    }

    @GetMapping("/admin")
    public ResponseEntity<ApiResponse> getAllComments(HttpServletRequest request) {
        return ResponseUtil.buildResponse(request, commentService.getAllComments());
    }

}
