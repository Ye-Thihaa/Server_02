package com.example.server.Config;

import java.util.Map;

public class ApiResponse {

    private int success;
    private int code;
    private Map<String, Object> meta;
    private Object data;
    private String message;

    // Constructor with all fields
    public ApiResponse(int success, int code, Map<String, Object> meta, Object data, String message) {
        this.success = success;
        this.code = code;
        this.meta = meta;
        this.data = data;
        this.message = message;
    }

    // Getters and setters for all fields
    public int getSuccess() { return success; }
    public void setSuccess(int success) { this.success = success; }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public Map<String, Object> getMeta() { return meta; }
    public void setMeta(Map<String, Object> meta) { this.meta = meta; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    // Builder pattern manually
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private int success;
        private int code;
        private Map<String, Object> meta;
        private Object data;
        private String message;

        public Builder success(int success) { this.success = success; return this; }
        public Builder code(int code) { this.code = code; return this; }
        public Builder meta(Map<String, Object> meta) { this.meta = meta; return this; }
        public Builder data(Object data) { this.data = data; return this; }
        public Builder message(String message) { this.message = message; return this; }

        public ApiResponse build() {
            return new ApiResponse(success, code, meta, data, message);
        }
    }
}

