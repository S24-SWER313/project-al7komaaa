package com.project1.project.Entity.Like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.project1.project.Entity.Post.Post;
import com.project1.project.Entity.User.User;


public interface LikeRepo extends JpaRepository<Like, Long> { 
    // @Query("SELECT l FROM Like l WHERE l.user = ?1")
List<Like> findByUser(User user);
List<Like> findByPost(Post post);
	
	
}