package com.example.server.Repository;

import com.example.server.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long postId);
    Optional<Post> findByIdAndUserId(final Long postId, final UUID userId);
    List<Post> findByVisibility(final String visibility);
}
