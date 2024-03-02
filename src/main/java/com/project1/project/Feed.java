package com.project1.project;

import java.util.List;

import jakarta.persistence.Entity;
import lombok.Data;
@Entity
@Data
public class Feed {
    private List<Post> posts;

    public Feed(List<Post> posts) {
        this.posts = posts;
    }
    
}
