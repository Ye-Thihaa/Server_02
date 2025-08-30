package com.example.server.dto.Response;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class ProfileResponseDto {
    private Long profileId;
    private UUID userId;
    private String name;
    private String studentId;
    private String email;
    private String bio;
    private String avatarUrl;
    private String location;
    private String coverUrl;
    private String batch;
    private String year;
    private String phone;
    private LocalDate birthDate;
    private boolean isPrivate;
    
}
