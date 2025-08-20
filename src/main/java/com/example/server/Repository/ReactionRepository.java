package com.example.server.Repository;

import com.example.server.Model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    List<Reaction> findByPostId(Long postId);
    Optional<Reaction> findByPostIdAndUserId(final Long postId,final Long userId);
}
