package com.example.server.Controller;

import com.example.server.Config.ApiResponse;
import com.example.server.Config.util.ResponseUtil;
import com.example.server.Service.FollowService;
import com.example.server.dto.Request.FollowRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow")
@CrossOrigin(origins = "https://orbi-uit.vercel.app")
public class FollowRestController {
    
    private final FollowService followService;
    
    public FollowRestController(FollowService followService) {
        this.followService = followService;
    }
    
    @PostMapping("/request/{requestId}/{responseId}")
    public ResponseEntity<ApiResponse> makeFollow(@PathVariable("requestId") Long requestId, @PathVariable("responseId") Long responseId, HttpServletRequest request) {
        FollowRequestDto requestDto = new FollowRequestDto(requestId, responseId);
        requestDto.setRequestUserId(requestId);
        requestDto.setResponseUserId(responseId);
        return ResponseUtil.buildResponse(request, followService.makeFollow(requestDto));
    }
}
