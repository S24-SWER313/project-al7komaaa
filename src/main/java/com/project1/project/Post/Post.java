package com.project1.project.Post;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project1.project.Comment.Comment;
import com.project1.project.Like.Like;
import com.project1.project.Share.Share;
import com.project1.project.User.User;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

private Type type;
  @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post") //t8ayarat
    private List<Like> like;

    @OneToMany(mappedBy = "post")
    private List<Share> shares;

    public Post(Long postId, Type type, User user, List<Comment> comments, List<Like> like, List<Share> shares) {
        this.postId = postId;
        this.type = type;
        this.user = user;
        this.comments = comments;
        this.like = like;
        this.shares = shares;
    }

    public Post() {
    }

    public Post(Long postId, Type type, User user) {
        this.postId = postId;
        this.type = type;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Post [postId=" + postId + ", type=" + type + "]";
    }



}

