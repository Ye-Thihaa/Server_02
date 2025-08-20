package com.example.server.Repository;

import com.example.server.Model.Comment;
import com.example.server.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long postId);
    Optional<Post> findByIdAndUserId(final Long postId, final Long userId);
}
