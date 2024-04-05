package com.project1.project.Controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project1.project.Entity.Notification.Notification;
import com.project1.project.Entity.Notification.NotificationRepository;
import com.project1.project.Entity.User.User;
import com.project1.project.Entity.User.UserRepo;

@Controller
public class NotificationController {


    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepo userRepository;

    @GetMapping("/notifications/{userId}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Long userId) {
        User recipient = userRepository.findById(userId).orElse(null);
        if (recipient == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Notification> notifications = notificationRepository.findByRecipientOrderByTimestampDesc(recipient);
        return ResponseEntity.ok(notifications);
    }


    @GetMapping("/notifications/unreadCount/{userId}")
    public ResponseEntity<Long> getUnreadNotificationCount(@PathVariable Long userId) {
        User recipient = userRepository.findById(userId).orElse(null);
        if (recipient == null) {
            return ResponseEntity.badRequest().build();
        }

        Long unreadCount = notificationRepository.countByRecipientAndIsRead(recipient, false);
        return ResponseEntity.ok(unreadCount);
    }


    @PutMapping("/notifications/markAsRead/{notificationId}")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable Long notificationId) {
        Optional<Notification> notificationOptional = notificationRepository.findById(notificationId);
        if (!notificationOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Notification not found");
        }

        Notification notification = notificationOptional.get();
        notification.setRead(true);
        notificationRepository.save(notification);
        
        return ResponseEntity.ok("Notification marked as read");
    }





    @PostMapping("/sendNotification/{recipientId}")
    public ResponseEntity<String> sendNotification(@PathVariable Long recipientId, @RequestBody String content) {
        User recipient = userRepository.findById(recipientId).orElse(null);
        if (recipient == null) {
            return ResponseEntity.badRequest().body("Recipient not found");
        }

        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setContent(content);
        notification.setTimestamp(LocalDateTime.now());
        notification.setRead(false);
        
        notificationRepository.save(notification);
        
        return ResponseEntity.ok("Notification sent successfully");
    }


    
}


    
