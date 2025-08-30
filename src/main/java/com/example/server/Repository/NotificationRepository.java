package com.example.server.Repository;

import com.example.server.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = """
        SELECT n.id, n.type, n.post_id, n.created_at, n.seen,
               p.avatar_url,
               au.raw_user_meta_data ->> 'full_name' AS from_name
        FROM notifications n
        LEFT JOIN profiles p ON p.user_id = n.notification_from
        LEFT JOIN auth.users au ON au.id = n.notification_from
        WHERE n.notification_for = :userId
        ORDER BY n.created_at DESC
    """, nativeQuery = true)
    List<Object[]> findUserNotificationFor(@Param("userId") UUID userId);
}
