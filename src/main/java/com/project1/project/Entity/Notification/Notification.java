package com.project1.project.Entity.Notification;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project1.project.Entity.User.User;

import jakarta.persistence.*;
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @JsonIgnore
    @ManyToOne
    private User recipient;

    
    private boolean isRead;

    @ManyToOne
    @JsonIgnore
    private User sender;

    private LocalDateTime timestamp;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // public User getRecipient() {
    //     return recipient;
    // }

    public String getRecipientUserName() {
        return recipient.getUsername();
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    // public User getSender() {
    //     return sender;
    // }

    public String getSenderUserName() {
        return sender.getUsername();
    }
    public void setSender(User sender) {
        this.sender = sender;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Notification() {
    }

   
}
