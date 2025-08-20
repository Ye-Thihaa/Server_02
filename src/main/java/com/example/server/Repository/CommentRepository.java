package com.example.server.Repository;

import com.example.server.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    Optional<Comment> findById(Long commentId);
    Optional<Comment> findByUserIdAndId(Long userId, Long commentId);
}
