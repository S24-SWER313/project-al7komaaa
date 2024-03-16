package com.project1.project.Entity.Post;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project1.project.Entity.Comment.Comment;

import java.util.List;


public interface PostRepo extends JpaRepository<Post, Long> { 
    

//Optional<Post>userpost;
 @Query("SELECT c FROM Comment c WHERE c.post.postId = ?1")
List<Comment>postComments(Long postId);
}
