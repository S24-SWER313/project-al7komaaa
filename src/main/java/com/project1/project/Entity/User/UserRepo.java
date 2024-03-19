package com.project1.project.Entity.User;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project1.project.Entity.Post.Post;
@Repository
public interface UserRepo extends JpaRepository<User, Long> { 



    List<User> findByFirstname(String firstname);
    List< User> findByLastname(String lastname);
    // List <User> findByGetName(String name);
    @Query("SELECT u FROM User u WHERE CONCAT(u.firstname, ' ', u.lastname) = ?1")
    List<User> findByFullname(String fullname);
// User findUserByJwtUser(String jwt);
@Query("SELECT p FROM Post p WHERE p.user.id = :userId")
List<Post> findPostsByUserId(Long userId);

    Optional<User> findByUsername(String username);
   

    // Optional<User> findByUserName(String userName);

    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    @Query("SELECT u.friends FROM User u WHERE u.id = :id")
    List<User> getFriends(Long id);
  
}