package com.example.server.Model;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Refers to profiles.id (not uitusers.id anymore)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ")
    private ZonedDateTime createdAt = ZonedDateTime.now();
    
//    @Column(name = "shared_post_id", nullable = true)
//    private Long sharedPost;
    @ManyToOne
    @JoinColumn(name = "shared_post_id", referencedColumnName = "id", nullable = true)
    private Post sharedPost;

    @Column(length = 20)
    private String visibility = "all"; // new & event, job portal, lost & found

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "uploaded_at", columnDefinition = "TIMESTAMPTZ")
    private ZonedDateTime uploadedAt = ZonedDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Post getSharedPost() {
        return sharedPost;
    }

    public void setSharedPost(Post sharedPost) {
        this.sharedPost = sharedPost;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public ZonedDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(ZonedDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

}

