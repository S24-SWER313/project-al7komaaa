package com.project1.project;

import java.util.Date;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Message {
    private @Id @GeneratedValue
     Long messageID;
     @ManyToOne
    private Long senderUserID;
    @ManyToOne
    private Long receiverUserID;
    private String messageText;
    private Date messageDate;
    public Message(Long messageID, Long senderUserID, Long receiverUserID, String messageText, Date messageDate) {
        this.messageID = messageID;
        this.senderUserID = senderUserID;
        this.receiverUserID = receiverUserID;
        this.messageText = messageText;
        this.messageDate = messageDate;
    }
    public Message() {
    }

    
}
