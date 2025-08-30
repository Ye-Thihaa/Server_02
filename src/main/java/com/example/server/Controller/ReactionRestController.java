package com.example.server.Controller;

import com.example.server.Config.ApiResponse;
import com.example.server.Config.util.ResponseUtil;
import com.example.server.Service.ReactionService;
import jakarta.servlet.http.HttpServletRequest;
import com.example.server.dto.Request.ReactionRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reactions")
@CrossOrigin(origins = "https://orbi-uit.vercel.app")
public class ReactionRestController {

    private final ReactionService reactionService;

    @PutMapping("/react/post/{postId}/user/{userId}")
    public ResponseEntity<ApiResponse> react(@PathVariable("userId") String user_id, @PathVariable("postId") Long post_id, @RequestBody ReactionRequestDto reactionRequestDto, HttpServletRequest request) {
        reactionRequestDto.setUserId(user_id);
        reactionRequestDto.setPostId(post_id);
        return ResponseUtil.buildResponse(request, reactionService.setReaction(reactionRequestDto));
    }
    
    @DeleteMapping("/react/user/{userId}/post/{postId}")
    public ResponseEntity<ApiResponse> deleteReaction(@PathVariable("userId") String userId, @PathVariable("postId") Long postId, HttpServletRequest request) {
        return ResponseUtil.buildResponse(request, reactionService.deleteReaction(postId, userId));
    }
    
    @GetMapping("/get-reactions")
    public ResponseEntity<ApiResponse> getReact(HttpServletRequest request) {
        return ResponseUtil.buildResponse(request, reactionService.getAllReaction());
    }
}
