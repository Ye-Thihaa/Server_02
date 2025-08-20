package com.example.server.Repository;

import com.example.server.Model.Notification;
import com.example.server.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByNotificationFor(User notificationFor);
}
