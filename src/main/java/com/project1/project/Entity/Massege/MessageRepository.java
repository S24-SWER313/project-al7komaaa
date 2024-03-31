package com.project1.project.Entity.Massege;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project1.project.Entity.User.User;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySenderAndReceiver(User sender, User receiver);
    
    @Query("SELECT m FROM Message m WHERE m.sender.id = :senderId")
    List<Message> findBySenderId(@Param("senderId") Long senderId);
    @Query("SELECT m FROM Message m WHERE (m.sender = :sender AND m.receiver = :receiver) OR (m.sender = :receiver AND m.receiver = :sender) ORDER BY m.timestamp ASC")
    List<Message> findMessagesBetweenUsers(@Param("sender") User sender, @Param("receiver") User receiver);
}