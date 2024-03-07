package com.project1.project.Like;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project1.project.Comment.Comment;
import com.project1.project.Post.Post;
import com.project1.project.User.User;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
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

    public Like(Long likeId, likeType type, Post post, User user) {
      this.likeId = likeId;
      this.type = type;
      this.post = post;
   
      this.user = user;
    }

    public Like() {
    }

    public Like(Long likeId, likeType type, Post post, Comment comment, User user) {
      this.likeId = likeId;
      this.type = type;
      this.post = post;
      this.comment = comment;
      this.user = user;
    }

    @Override
    public String toString() {
      return "Like [likeId=" + likeId + ", type=" + type + "]";
    }
        
    
}
