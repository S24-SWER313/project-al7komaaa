package com.project1.project.Post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project1.project.Like.Like;

public interface PostRepo extends JpaRepository<Post, Long> { 
    //   @Query("SELECT p.like FROM Post p WHERE p.id = :postId")
//  List<Like> findByLike(Post post);


}
// public interface PostRepo extends JpaRepository<Post, Long> {
//     // Adjust the method signature to accept a postId
//     @Query("SELECT l FROM Like l WHERE l.post.id = :postId")
//     List<Like> findByPostIdLikes(@Param("postId") Long postId);
// }
