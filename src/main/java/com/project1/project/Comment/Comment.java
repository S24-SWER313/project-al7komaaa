package com.project1.project.Comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project1.project.Post.Post;
import com.project1.project.Post.Type;
import com.project1.project.User.User;

import jakarta.persistence.*;


@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    private Type type;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
  @JsonIgnore
     @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(Long commentId, Type type, User user, Post post) {
        this.commentId = commentId;
        this.type = type;
        this.user = user;
        this.post = post;
    }

    public Comment() {
    }

    @Override
    public String toString() {
        return "Comment [commentId=" + commentId + ", type=" + type + "]";
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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
