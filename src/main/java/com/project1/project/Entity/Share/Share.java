package com.project1.project.Entity.Share;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project1.project.Entity.Post.Post;
import com.project1.project.Entity.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
@Entity
@Table(name = "shares")
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shareId;
    @Size(max = 10000, message = "Content is too long")
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

    public Share( String content, User user, Post post) {
       
        this.content = content;
        this.user = user;
        this.post = post;
    }
    @Override
    public String toString() {
        return "Share [shareId=" + shareId + ", content=" + content + "]";
    }
    public Long getShareId() {
        return shareId;
    }
    public void setShareId(Long shareId) {
        this.shareId = shareId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Post getPost() {
        return post;
    }
    public void setPost(Post post) {
        this.post = post;
    }
    
    
    
}