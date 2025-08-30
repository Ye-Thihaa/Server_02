package com.example.server.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;
    
    private String location;

    @Column(columnDefinition = "TIMESTAMPTZ DEFAULT now()")
    private OffsetDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMPTZ DEFAULT now()")
    private OffsetDateTime updatedAt;

    @Column(name = "batch", length = 50, nullable = false)
    private String batch = "none";

    @Column(name = "year", length = 50, nullable = false)
    private String year = "none";

    @Column(name = "phone", length = 50, nullable = false)
    private String phone = "none";

    @Column(name = "cover_url", length = 50, nullable = false)
    private String coverUrl = "default.png";

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "is_private", nullable = false)
    private Boolean isPrivate = false;


}
