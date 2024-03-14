package com.project1.project.Entity.Like;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project1.project.Entity.Comment.Comment;
import com.project1.project.Entity.Post.Post;
import com.project1.project.Entity.User.User;

import jakarta.persistence.*;
@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    private likeType type ;
    @JsonIgnore  @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
  @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Like( likeType type, Post post, User user) {
      
      this.type = type;
      this.post = post;
   
      this.user = user;
    }
    public Like() {
    }

    public Like( likeType type, Post post, Comment comment, User user) {
     
      this.type = type;
      this.post = post;
      this.comment = comment;
      this.user = user;
    }
    @Override
    public String toString() {
      return "Like [likeId=" + likeId + ", type=" + type + "]";
    }
    public Long getLikeId() {
      return likeId;
    }
    public void setLikeId(Long likeId) {
      this.likeId = likeId;
    }
    public likeType getType() {
      return type;
    }
    public void setType(likeType type) {
      this.type = type;
    }
    public Post getPost() {
      return post;
    }
    public void setPost(Post post) {
      this.post = post;
    }
    public Comment getComment() {
      return comment;
    }
    public void setComment(Comment comment) {
      this.comment = comment;
    }
    public User getUser() {
      return user;
    }
    public void setUser(User user) {
      this.user = user;
    }
    
    
    
    
}