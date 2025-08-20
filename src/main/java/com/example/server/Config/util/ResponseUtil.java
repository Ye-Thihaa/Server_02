package com.example.server.Config.util;

import com.example.server.Config.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

    public static ResponseEntity<ApiResponse> buildResponse(HttpServletRequest request, ApiResponse response) {
        if (response.getMeta() == null) {
            Map<String, Object> meta = new HashMap<>();
            meta.put("timestamp", ZonedDateTime.now().toString());
            meta.put("method", request.getMethod());
            meta.put("endpoint", request.getRequestURI());
            response.setMeta(meta);
        }

        HttpStatus status = HttpStatus.resolve(response.getCode());
        if (status == null) {
            status = HttpStatus.OK;
        }

        return new ResponseEntity<>(response, status);
    }
}
