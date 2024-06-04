package com.project1.project.Entity.User.Friend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project1.project.Entity.User.User;

import java.util.List;
import java.util.Optional;




public interface FriendRequestRepo extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByReceiverId(Long receiverId);
    Optional<FriendRequest> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);
    //void deleteBySenderAndReceiver(User sender, User receiver);
    //@Query("DELETE FROM friend_request fr WHERE fr.sender_id = :sender AND fr.receiver_id = :receiver")
    //void deleteBySenderAndReceiver(@Param("sender") Long sender_id, @Param("receiver") Long receiver_id);
  
      
        // Optional<FriendRequest> findBySenderAndReceiverOrReceiverAndSender(User sender1, User receiver1, User sender2, User receiver2);
        Optional<FriendRequest> findBySenderAndReceiverOrReceiverAndSender(User sender1, User receiver1, User sender2, User receiver2);
        void deleteBySenderAndReceiverOrReceiverAndSender(User sender1, User receiver1, User sender2, User receiver2);
    
    }


