package com.example.server.Service;

import com.example.server.Config.ApiResponse;
import com.example.server.Model.Comment;
import com.example.server.Model.Notification;
import com.example.server.Model.Post;
import com.example.server.Model.User;
import com.example.server.dto.Request.NotificationRequestDto;

import java.util.List;

public interface NotificationService {
    void notifyAllUsersExceptAuthor(User user, Post post, List<User> allUsers);
    void notifyPostOwner(User user, Post post, final String notification_Type);
    void notifyCommentOwner(User user, Comment parentComment, Post post);
    void notifyAdmin(User userRole, Post post, User user);
    List<Notification> getAllNotify();
    ApiResponse haveBeenSeen(NotificationRequestDto notificationRequestDto);
    ApiResponse fetchAssoNotification(final String userId);
    
}
