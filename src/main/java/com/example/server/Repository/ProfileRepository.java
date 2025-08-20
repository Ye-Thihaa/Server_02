package com.example.server.Repository;

import com.example.server.Model.Profile;
import com.example.server.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
//    Optional<Profile> findById(Long id);
    Optional<Profile> findByUser(User user);
}
