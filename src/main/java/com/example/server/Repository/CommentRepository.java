package com.example.server.Repository;

import com.example.server.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(final Long postId);
    Optional<Comment> findById(final Long commentId);
    Optional<Comment> findByUserIdAndId(final UUID userId,final Long commentId);
}
