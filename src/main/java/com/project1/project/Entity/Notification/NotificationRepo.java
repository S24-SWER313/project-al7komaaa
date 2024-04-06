package com.project1.project.Entity.Notification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project1.project.Entity.User.User;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientAndIsReadIsFalse(User recipient);
}
