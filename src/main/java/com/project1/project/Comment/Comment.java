package com.project1.project.Comment;

import java.util.Date;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
@Entity
@Data
public class Comment {
    private @Id @GeneratedValue
     Long commentID;
     @ManyToOne
    private Long postID;
    @ManyToOne
    private Long userID;
    private String commentText;
    private Date commentDate;
    public Comment(Long commentID, Long postID, Long userID, String commentText, Date commentDate) {
        this.commentID = commentID;
        this.postID = postID;
        this.userID = userID;
        this.commentText = commentText;
        this.commentDate = commentDate;
    }
    public Comment() {
    }

    
}
