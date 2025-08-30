package com.example.server.dto.Request;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class UserRequestDto {
    private String username;
    private String email;
    private String password;
    private String studentId;
    
}
