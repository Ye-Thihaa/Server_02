package com.example.server.Repository;

import com.example.server.Model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    List<Reaction> findByPostId(Long postId);
    Optional<Reaction> findByPostIdAndUserId(final Long postId,final UUID userId);
}
