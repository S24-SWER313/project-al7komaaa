package com.project1.project.post;

import java.util.List;

import com.project1.project.comment.Comment;
import com.project1.project.like.Like;
import com.project1.project.share.Share;
import com.project1.project.user.User;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    // Other post properties

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post") //t8ayarat
    private List<Like> like;

    @OneToMany(mappedBy = "post")
    private List<Share> shares;
}

