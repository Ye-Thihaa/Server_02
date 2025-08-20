package com.example.server.Controller;

import com.example.server.Config.ApiResponse;
import com.example.server.Config.util.ResponseUtil;
import com.example.server.Model.Notification;
import com.example.server.Service.NotificationService;
import com.example.server.dto.Request.NotificationRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "https://orbi-uit.vercel.app")
public class NotificationRestController {
    
    private final NotificationService notificationService;
    
    public NotificationRestController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    @GetMapping
    public List<Notification> getNotifications() {
        return notificationService.getAllNotify();
    }
    
    @PatchMapping("/seen/{receiverId}")
    public ResponseEntity<ApiResponse> seenNotification(@PathVariable("receiverId") Long receiverId, @RequestBody NotificationRequestDto notificationRequestDto, HttpServletRequest request) {
        notificationRequestDto.setNotificationFor(receiverId);
        return ResponseUtil.buildResponse(request, notificationService.haveBeenSeen(notificationRequestDto));
    }
}
