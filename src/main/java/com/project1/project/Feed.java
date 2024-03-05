package com.project1.project;

import java.util.List;

import com.project1.project.Post.Post;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
@Entity
@Data
public class Feed {
    @Id
    private List<Post> posts;

    public Feed(List<Post> posts) {
        this.posts = posts;
    }
    
}
