package com.project1.project;

import java.util.Date;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import lombok.Data;
@Entity
@Data
public class Post {
    private @Id @GeneratedValue
    Long postID;
     @ManyToOne
    private Long userID;
    private String content; // بلزم نعمل الها كلاس اينوم سؤال
    private Date postDate;
    private Long likesCount;

    private Long commentsCount;
    private Long sharesCount;
    public Post(Long postID, Long userID, String content, Date postDate, Long likesCount, Long commentsCount,
    Long sharesCount) {
        this.postID = postID;
        this.userID = userID;
        this.content = content;
        this.postDate = postDate;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.sharesCount = sharesCount;
    }
    public Post() {
    }   
     
}
