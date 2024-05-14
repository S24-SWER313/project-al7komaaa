package com.project1.project.Entity.Post;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project1.project.Entity.Comment.Comment;
import com.project1.project.Entity.Like.Like;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> { 
    

//Optional<Post>userpost;
@Query("SELECT c FROM Comment c WHERE c.post.postId = ?1")
List<Comment>postComments(Long postId);



@Query("SELECT l FROM Like l WHERE l.post.postId = ?1")
List<Like> findLikes(Long postId);
@Query(value = "SELECT * FROM posts ORDER BY RAND() LIMIT 10", nativeQuery = true)
List<Post> findRandom5Posts();
}