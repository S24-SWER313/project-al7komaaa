package com.project1.project.share;

import com.project1.project.post.Post;
import com.project1.project.user.User;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "shares")
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shareId;

    // Other share properties

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    
}
