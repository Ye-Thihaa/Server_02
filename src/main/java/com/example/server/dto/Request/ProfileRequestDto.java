package com.example.server.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
public class ProfileRequestDto {
    private String userId;
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
