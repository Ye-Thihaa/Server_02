package com.example.server.Repository;

import com.example.server.Model.Follow;
import com.example.server.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
    boolean existsByFollowerIdAndFolloweeId(Long follower, Long followee);
}
