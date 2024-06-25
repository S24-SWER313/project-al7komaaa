package com.project1.project.Entity.Like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.project1.project.Entity.Post.Post;
import com.project1.project.Entity.User.User;



public interface LikeRepo extends JpaRepository<Like, Long> { 
    // @Query("SELECT l FROM Like l WHERE l.user = ?1")
List<Like> findByUser(User user);
List<Like> findByPost(Post post);
@Query(value = "SELECT * FROM likes WHERE post_id = :postId ORDER BY RAND() LIMIT 5", nativeQuery = true)
List<Like> findRandom5LikesByLikeId(@Param("postId") Long postId);

@Query(value = "SELECT COUNT(*) FROM likes WHERE post_id = :postId", nativeQuery = true)
Long countLikesByLikeId(@Param("postId") Long postId);
List<Like> findByUserAndPost(User user, Post post);


}