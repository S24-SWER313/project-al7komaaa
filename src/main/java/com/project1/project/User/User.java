

package com.project1.project.user;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

import com.project1.project.comment.Comment;
import com.project1.project.like.Like;
import com.project1.project.post.Post;
import com.project1.project.share.Share;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Other user properties

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    private List<Like> likes;

    @OneToMany(mappedBy = "user")
    private List<Share> shares;
   
}
