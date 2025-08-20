package com.example.server.Controller;

import com.example.server.Config.ApiResponse;
import com.example.server.Config.util.ResponseUtil;
import com.example.server.Repository.PostRepository;
import com.example.server.Service.PostService;
import com.example.server.dto.Request.PostRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "https://orbi-uit.vercel.app")
public class PostRestController {
    
    private final PostService postService;
    private final PostRepository postRepository;

    public PostRestController(PostService postService, PostRepository postRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
    }

    // Create post for user (C)
    @PutMapping("/create-post/{id}")
    public ResponseEntity<ApiResponse> createPost(@PathVariable("id") Long id, @RequestBody PostRequestDto postRequestDto) {
        postRequestDto.setId(id);
        ApiResponse apiResponse = postService.createPost(postRequestDto);
        if (apiResponse != null) {
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all posts with reactions/comments (R)
    @GetMapping("/call-posts")
    public ResponseEntity<ApiResponse> getAllPosts(HttpServletRequest request) {
        return ResponseUtil.buildResponse(request, postService.getAllPosts());
    }
    
    // post sharing (S)
    @PostMapping("/user/{sharedUserId}/share/{originalPostId}")
    public ResponseEntity<ApiResponse> sharePost(@PathVariable("sharedUserId") Long sharedUserId, @PathVariable("originalPostId") Long originalPostId, @RequestBody PostRequestDto postRequestDto, HttpServletRequest request) {
        postRequestDto.setId(sharedUserId);
        postRequestDto.setSharedPostId(originalPostId);
        return ResponseUtil.buildResponse(request, postService.sharedPost(postRequestDto));
    }
    
    // post updating (U)
    @PatchMapping("/user/{userId}/post/{postId}")
    public ResponseEntity<ApiResponse> updatePosts(@PathVariable("userId") Long userId, @PathVariable("postId") Long postId, @RequestBody PostRequestDto postRequestDto, HttpServletRequest request) {
        postRequestDto.setId(userId);
        postRequestDto.setPostId(postId);
        return ResponseUtil.buildResponse(request, postService.updatedPosts(postRequestDto));
    }
    
    // post deleting (D)
    @DeleteMapping("/user/{userId}/post/{postId}")
    public ResponseEntity<ApiResponse> deletePosts(@PathVariable("userId") Long userId, @PathVariable("postId") Long postId, HttpServletRequest request){
        return ResponseUtil.buildResponse(request, postService.deletePosts(userId, postId));
    }
}
