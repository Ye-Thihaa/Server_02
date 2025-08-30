package com.example.server.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    private UUID id; // connected with auth.users in Supabase

    @Column(nullable = false, unique = true)
    private String studentId;
    
    @Column(name = "username")
    private String username;

    @Column(name = "role", nullable = false)
    private String role = "USER";

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
