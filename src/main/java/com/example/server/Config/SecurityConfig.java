//package com.example.server.Config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()
//                )
//                .csrf(csrf -> csrf.disable())
//                .formLogin(login -> login.disable())
//                .httpBasic(basic -> basic.disable());
//
//        return http.build();
//    }
//}

package com.example.server.Config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final SupabaseJwtFilter supabaseJwtFilter;

    public SecurityConfig(SupabaseJwtFilter supabaseJwtFilter) {
        this.supabaseJwtFilter = supabaseJwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF since we're using JWT
                .csrf(csrf -> csrf.disable())

                // No session - stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Exception handling for unauthorized users
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                        )
                )

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()  // Open endpoint
                        .requestMatchers("/api/v1/auth/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/posts/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/comments/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/notifications/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/profiles/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/reactions/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()                   // Others need token
                )

                // Add our JWT filter before username/password filter
                .addFilterBefore(supabaseJwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

