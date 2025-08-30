package com.example.server.dto.Response;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class UserResponseDto {
    private String accessToken;
    private String refreshToken;
    private String message;
}
