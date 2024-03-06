package com.project1.project.comment;

import com.project1.project.post.Post;
import com.project1.project.user.User;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data 
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    // Other comment properties

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

     @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

}
