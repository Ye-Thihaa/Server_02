package com.example.server.GlobalContainer;

public class GlobalUser {
    private static Long currentUserId;

    public static void setCurrentUserId(Long userId) {
        currentUserId = userId;
    }

    public static Long getCurrentUserId() {
        return currentUserId;
    }

    public static void clear() {
        currentUserId = null;
    }
}

