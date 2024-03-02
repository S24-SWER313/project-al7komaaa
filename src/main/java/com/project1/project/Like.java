package com.project1.project;

import java.util.Date;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import lombok.Data;
@Entity
@Data
public class Like {
    private @Id @GeneratedValue
     Long likeID;
     @ManyToOne
    private Long postID;
    @ManyToOne
    private Long userID;
    private Date likeDate;
    public Like() {
    }
    public Like(Long likeID, Long postID, Long userID, Date likeDate) {
        this.likeID = likeID;
        this.postID = postID;
        this.userID = userID;
        this.likeDate = likeDate;
    }
   
    
}
