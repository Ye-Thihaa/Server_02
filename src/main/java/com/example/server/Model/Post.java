package com.example.server.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TIMESTAMPTZ DEFAULT now()")
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private String visibility = "all";

    private String imageUrl;

    @Column(columnDefinition = "TIMESTAMPTZ DEFAULT now()")
    private OffsetDateTime uploadedAt;
    
    @Column(name = "post_status", nullable = false)
    private String postStatus = "finished";

    @ManyToOne
    @JoinColumn(name = "shared_post_id")
    private Post sharedPost;
    
}
