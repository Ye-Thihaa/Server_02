package com.example.server.dto.Request;

public class NotificationRequestDto {
    private Long id;
    private Long notificationFor;
    private Long notificationFrom;
    private String type;
    private Boolean seen;
    
    public NotificationRequestDto(Long id, Long notificationFor, Long notificationFrom, String type, Boolean seen) {
        this.id = id;
        this.notificationFor = notificationFor;
        this.notificationFrom = notificationFrom;
        this.type = type;
        this.seen = seen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNotificationFor() {
        return notificationFor;
    }

    public void setNotificationFor(Long notificationFor) {
        this.notificationFor = notificationFor;
    }

    public Long getNotificationFrom() {
        return notificationFrom;
    }

    public void setNotificationFrom(Long notificationFrom) {
        this.notificationFrom = notificationFrom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }
}
