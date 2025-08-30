package com.example.server.Repository;

import com.example.server.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
//    boolean existsByEmail(String email);
//    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
    Optional<User> findByRole(final String role);
}
