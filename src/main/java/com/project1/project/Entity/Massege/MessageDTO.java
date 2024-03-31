package com.project1.project.Entity.Massege;

import com.project1.project.Entity.User.User;

public class MessageDTO {
    private Long sender;
    private Long receiver;
    private String content;
    
    public MessageDTO( String content) {
               this.content = content;
    }
    public Long getSender() {
        return sender;
    }
    public void setSender(Long sender) {
        this.sender = sender;
    }
    public Long getReceiver() {
        return receiver;
    }
    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

}
