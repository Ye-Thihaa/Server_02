package com.example.server.Service.Impl;

import com.example.server.Config.ApiResponse;
import com.example.server.Config.exceptions.EntityNotFoundException;
import com.example.server.Model.Comment;
import com.example.server.Model.Notification;
import com.example.server.Model.Post;
import com.example.server.Model.User;
import com.example.server.Repository.NotificationRepository;
import com.example.server.Service.NotificationService;
import com.example.server.dto.Request.NotificationRequestDto;
import com.example.server.dto.Response.NotificationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    
    // get all notification data
    @Override
    @Transactional(readOnly = true)
    public List<Notification> getAllNotify() {
        return notificationRepository.findAll();
    }

    // fetch notifications that is associated with each user
    @Override
    @Transactional(readOnly = true)
    public ApiResponse fetchAssoNotification(final String userId) {
        List<Object[]> results = notificationRepository.findUserNotificationFor(UUID.fromString(userId));

        List<NotificationResponseDto> notifications = results.stream()
                .map(obj -> new NotificationResponseDto(  // id, type, fromName, fromAvatar, postId, createdAt, seen
                        ((Number) obj[0]).longValue(),
                        (String) obj[1],
                        (String) obj[6],
                        (String) obj[5],
                        obj[2] != null ? ((Number) obj[2]).longValue() : null,
                        obj[3] != null ? ((java.sql.Timestamp) obj[3])
                                .toInstant()
                                .atOffset(java.time.ZoneOffset.UTC) : null,
                        obj[4] != null && (Boolean) obj[4]
                ))
                .toList();

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .data(notifications)
                .message("Fetched notifications successfully")
                .build();
    }

    // notify admin that relate with news & events post 
    @Override
    public void notifyAdmin(User userRole, Post post, User user) {
        if(Objects.equals(userRole.getRole(), "ADMIN")) {
            Notification notifyAdmin = new Notification();
            notifyAdmin.setNotificationFor(userRole);
            notifyAdmin.setNotificationFrom(user);
            notifyAdmin.setType("news & events");
            notifyAdmin.setPost(post);
            notificationRepository.save(notifyAdmin);
            
            messagingTemplate.convertAndSend("/topic/admin", notifyAdmin);
        }
    }

    // notify all users expect owner 
    @Override
    public void notifyAllUsersExceptAuthor(User author, Post post, List<User> allUsers) {
        for (User user : allUsers) {
            if (user.getId() != author.getId()) {
                Notification notification = new Notification();
                notification.setNotificationFor(user);
                notification.setNotificationFrom(author);
                notification.setType("post");
                notification.setPost(post);
                notificationRepository.save(notification);
                
                // real-time push to Web Socket
                messagingTemplate.convertAndSendToUser(user.getStudentId(), "/queue/notifications", notification);
            }
        }
    }
    
    // notify post owner, who is comment at your post
    @Override
    public void notifyPostOwner(User user, Post post, String notification_Type) {
        if (user.getId() != post.getUser().getId()) {
            Notification notification = new Notification();
            notification.setNotificationFor(post.getUser());
            notification.setNotificationFrom(user);
            notification.setType(notification_Type);
            notification.setPost(post);
            
            messagingTemplate.convertAndSendToUser(post.getUser().getStudentId(), "/queue/notifications", notification);
        }
    }
    
    // notify comment owner , who is comment at your comment
    @Override
    public void notifyCommentOwner(User user, Comment parentComment, Post post) {
        if (user.getId() != parentComment.getUser().getId()) {
            Notification notification = new Notification();
            notification.setNotificationFor(parentComment.getUser());
            notification.setNotificationFrom(user);
            notification.setType("recomment");
            notification.setPost(post);
            
            messagingTemplate.convertAndSendToUser(parentComment.getUser().getStudentId(), "/queue/notifications", notification);
        }
    }
    
    // notifications have already been seen by user 
    @Override
    public ApiResponse haveBeenSeen(NotificationRequestDto notificationRequestDto) {

        // Fetch existing notification by ID
        Notification notification = notificationRepository.findById(notificationRequestDto.getNotificationFor())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Notification not found with id " + notificationRequestDto.getNotificationFor()));

        // Mark as seen
        notification.setSeen(true);
        notificationRepository.save(notification);

        // Prepare response
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("notification_for", notification.getNotificationFor().getId());
        responseData.put("notification_from", notification.getNotificationFrom() != null ? notification.getNotificationFrom().getId() : null);
        responseData.put("type", notification.getType());
        responseData.put("post", notification.getPost() != null ? notification.getPost().getId() : null);
        responseData.put("created_at", notification.getCreatedAt());
        responseData.put("seen", notification.isSeen());

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(responseData)
                .message("Notification seen")
                .build();
    }
    
}
