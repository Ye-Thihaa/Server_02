package com.example.server.dto.Response;

import lombok.*;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class NotificationResponseDto {
    private Long id;
    private String type;
    private String fromName;
    private String fromAvatar;
    private Long postId;
    private OffsetDateTime createdAt;
    private boolean seen;
    
}
