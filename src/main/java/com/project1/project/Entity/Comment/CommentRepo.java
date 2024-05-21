package com.project1.project.Entity.Comment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project1.project.Entity.Post.Post;

public interface CommentRepo extends JpaRepository<Comment, Long> { 

@Query(value = "SELECT * FROM comments WHERE post_id = :postId ORDER BY RAND() LIMIT 5", nativeQuery = true)
List<Comment> findRandom5CommentsByPostId(@Param("postId") Long postId);
 
Long countCommentsByPostId(Long postId);
// @Query("SELECT c FROM comments c WHERE c.comment_id = :id")
Optional<Comment> findById(Long id);
}