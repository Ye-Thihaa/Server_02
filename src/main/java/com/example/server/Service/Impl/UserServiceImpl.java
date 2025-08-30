package com.example.server.Service.Impl;

import com.example.server.Config.ApiResponse;
import com.example.server.Model.User;
import com.example.server.Repository.UserRepository;
import com.example.server.Service.UserService;
import com.example.server.dto.Request.UserLoginRequestDto;
import com.example.server.dto.Request.UserRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final WebClient webClient;

    private final String serviceKey;
    private final String anonKey;

    public UserServiceImpl(UserRepository userRepository,
                           @Value("${supabase.url}") String supabaseUrl,
                           @Value("${supabase.anon.key}") String anonKey,
                           @Value("${supabase.service-key}") String serviceKey) { // Use anon key
        this.userRepository = userRepository;
        this.serviceKey = serviceKey; // SAVE FOR LATER USE
        this.anonKey = anonKey;       // SAVE FOR LATER USE

        this.webClient = WebClient.builder()
                .baseUrl(supabaseUrl + "/auth/v1")
                .defaultHeader("apikey", anonKey)  // Anon key is enough
                .defaultHeader("Authorization", "Bearer " + serviceKey) // Service role key
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Override
    public String signUp(UserRequestDto userRequestDto) {
        try {
            Map<String, Object> response = webClient.post()
                    .uri("/signup")
                    .header("Prefer", "return=minimal") // Required for custom redirect
                    .bodyValue(Map.of(
                            "email", userRequestDto.getEmail(),
                            "password", userRequestDto.getPassword(),
                            "options", Map.of(
                                    "data", Map.of(
                                            "username", userRequestDto.getUsername(),
                                            "studentId", userRequestDto.getStudentId()
                                    ),
                                    // Must be in Supabase → Authentication → Redirect URLs
                                    "emailRedirectTo", "https://orbi-uit.vercel.app"
                            )
                    ))
                    .retrieve()
                    .onStatus(status -> !status.is2xxSuccessful(), clientResponse ->
                            clientResponse.bodyToMono(String.class)
                                    .map(body -> new RuntimeException("Supabase error: " + body))
                    )
                    .bodyToMono(Map.class)
                    .block();

            System.out.println("Supabase response: " + response);

            if (response == null || !response.containsKey("id")) {
                return "Sign-up failed: no user ID returned";
            }

            // Save user to local DB
            User user = new User();
            user.setId(UUID.fromString((String) response.get("id")));
            user.setUsername(userRequestDto.getUsername());
            user.setStudentId(userRequestDto.getStudentId());
            user.setCreatedAt(LocalDateTime.now());
            userRepository.save(user);

            return "Sign-up successful! Please check your email to confirm.";

        } catch (Exception e) {
            e.printStackTrace();
            return "Sign-up failed: " + e.getMessage();
        }
    }
    
    @Override
    public ApiResponse login(UserLoginRequestDto userLoginRequestDto) {
        try {
            Map<String, Object> response = webClient.post()
                    .uri("/token?grant_type=password")
                    .bodyValue(Map.of(
                            "email", userLoginRequestDto.getEmail(),
                            "password", userLoginRequestDto.getPassword()
                    ))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || !response.containsKey("access_token")) {
                return ApiResponse.builder()
                        .success(0)
                        .code(401)
                        .message("Login failed: Invalid credentials")
                        .build();
            }

            // Extract user ID from response if available
            Map<String, Object> user = (Map<String, Object>) response.get("user");
            String userId = user != null ? (String) user.get("id") : null;
            
            // get role from the users table
            Optional<User> userRole = userRepository.findById(UUID.fromString(userId));
            String role = userRole.map(User::getRole).orElse("USER");

            // Wrap tokens and userId inside 'data' field
            Map<String, Object> data = Map.of(
                    "accessToken", response.get("access_token"),
                    "refreshToken", response.get("refresh_token"),
                    "userId", userId,   // <-- return user ID
                    "role", role
            );

            return ApiResponse.builder()
                    .success(1)
                    .code(200)
                    .data(data)
                    .message("Login successful")
                    .build();

        } catch (WebClientResponseException.BadRequest e) {
            return ApiResponse.builder()
                    .success(0)
                    .code(400)
                    .message("Login failed: " + e.getResponseBodyAsString())
                    .build();
        } catch (Exception e) {
            return ApiResponse.builder()
                    .success(0)
                    .code(500)
                    .message("Login failed: " + e.getMessage())
                    .build();
        }
    }
    
    @Override
    public boolean checkUserExists(String email) {
        try {
            String url = "https://fufvzfmeoxomrcpscccm.supabase.co/auth/v1/admin/users";

            Map<String, Object> response = webClient.get()
                    .uri(url)
                    .header("apikey", serviceKey)  // ✅ Now works fine
                    .header("Authorization", "Bearer " + serviceKey)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || !response.containsKey("users")) {
                return false;
            }

            List<Map<String, Object>> users = (List<Map<String, Object>>) response.get("users");

            return users.stream()
                    .anyMatch(user -> email.equalsIgnoreCase((String) user.get("email")));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Update user password in Supabase Auth
    @Override
    public boolean updatePassword(String email, String newPassword) {
        try {
            // Fetch user info by email
            Map<String, Object> user = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/admin/users")
                            .queryParam("email", email)
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (user == null || user.get("users") == null) {
                System.out.println("User not found in Supabase: " + email);
                return false;
            }

            // Extract user ID from the response
            List<Map<String, Object>> users = (List<Map<String, Object>>) user.get("users");
            if (users.isEmpty()) {
                System.out.println("No user found with email: " + email);
                return false;
            }
            String userId = (String) users.get(0).get("id");

            // Update password using the correct endpoint
            Map<String, String> body = Map.of("password", newPassword);

            webClient.put()
                    .uri("/admin/users/" + userId)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAllUsersWithStudentId() {
        // Call Supabase Admin API
        Map<String, Object> response = webClient.get()
                .uri("/admin/users")
                .retrieve()
                .bodyToMono(Map.class)   // ✅ Expect JSON object, not List
                .block();

        // Extract "users" array from Supabase response
        List<Map<String, Object>> authUsers = new ArrayList<>();
        if (response != null && response.containsKey("users")) {
            authUsers = (List<Map<String, Object>>) response.get("users");
        }

        // Prepare final wrapped data list
        List<Map<String, Object>> dataList = new ArrayList<>();

        for (Map<String, Object> authUser : authUsers) {
            String uuid = (String) authUser.get("id"); // Supabase user ID
            String studentId = null;
            String name = null;
            String is_admin = "USER";

            try {
                // Fetch studentId from local DB using Supabase UUID
                Optional<User> dbUser = userRepository.findById(UUID.fromString(uuid));
                if (dbUser.isPresent()) {
                    studentId = dbUser.get().getStudentId();
                    name = dbUser.get().getUsername();
                    is_admin = dbUser.get().getRole();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // ✅ Add studentId into user object
            Map<String, Object> userMap = new HashMap<>(authUser);
            userMap.put("studentId", studentId);
            userMap.put("userName", name);
            userMap.put("isAdmin", is_admin);

            // ✅ Create wrapper with "aud" and "users"
            Map<String, Object> wrapper = new HashMap<>();
            wrapper.put("aud", "authenticated");
            wrapper.put("users", List.of(userMap));  // Single user inside list

            dataList.add(wrapper);
        }

        return dataList;
    }

}
