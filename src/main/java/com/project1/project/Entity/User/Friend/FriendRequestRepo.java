package com.project1.project.Entity.User.Friend;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FriendRequestRepo extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByReceiverId(Long receiverId);
    Optional<FriendRequest> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
}