package com.example.server.Model;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "notification_for", nullable = false)
    private User notificationFor;

    @ManyToOne
    @JoinColumn(name = "notification_from")
    private User notificationFrom;

    private String type; // like, comment, share, post

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ")
    private ZonedDateTime createdAt = ZonedDateTime.now();

    private Boolean seen = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getNotificationFor() {
        return notificationFor;
    }

    public void setNotificationFor(User notificationFor) {
        this.notificationFor = notificationFor;
    }

    public User getNotificationFrom() {
        return notificationFrom;
    }

    public void setNotificationFrom(User notificationFrom) {
        this.notificationFrom = notificationFrom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }
}

