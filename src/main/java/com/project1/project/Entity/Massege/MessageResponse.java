package com.project1.project.Entity.Massege;

import java.time.LocalDateTime;

public class MessageResponse {
    private String senderName;
    private String content;
    private LocalDateTime timestamp;
 
    public MessageResponse(String senderName, String content, LocalDateTime timestamp) {
        this.senderName = senderName;
        this.content = content;
        this.timestamp = timestamp;
    }
    public MessageResponse() {
        //TODO Auto-generated constructor stub
    }
    public String getSenderName() {
        return senderName;
    }
    public void setSenderName(String receiverName) {
        this.senderName = receiverName;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}