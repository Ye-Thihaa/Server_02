package com.example.server.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(org.springframework.http.server.ServerHttpRequest request,
                                                      org.springframework.web.socket.WebSocketHandler wsHandler,
                                                      Map<String, Object> attributes) {

                        // Extract studentId from query parameter
                        String uri = request.getURI().toString();
                        String studentId = null;

                        if (uri.contains("studentId=")) {
                            studentId = uri.substring(uri.indexOf("studentId=") + 10);
                            if (studentId.contains("&")) {
                                studentId = studentId.substring(0, studentId.indexOf("&"));
                            }
                        }

                        // Fallback to session ID if no studentId is provided
                        if (studentId == null || studentId.isEmpty()) {
                            studentId = request.getRemoteAddress().toString();
                        }

                        String finalStudentId = studentId;
                        return () -> finalStudentId; // Set studentId as Principal name
                    }
                })
                .withSockJS();
    }
}
