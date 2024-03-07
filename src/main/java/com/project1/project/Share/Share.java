package com.project1.project.Share;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project1.project.Post.Post;
import com.project1.project.User.User;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "shares")
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shareId;
private String content;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
  @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Share() {
    }

    public Share(Long shareId, String content, User user, Post post) {
        this.shareId = shareId;
        this.content = content;
        this.user = user;
        this.post = post;
    }

    @Override
    public String toString() {
        return "Share [shareId=" + shareId + ", content=" + content + "]";
    }
    

    
}
