package com.project1.project.Entity.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    Set<User> getFriends(Long id);
    @Query("SELECT COUNT(f) FROM User u JOIN u.friends f WHERE u.id = :userId")
    Long countFriends(Long userId);
    // @Query("SELECT u FROM User u WHERE u.id NOT IN (SELECT f.id FROM User user JOIN user.friends f WHERE user.id = :userId) AND u.id != :userId")
    // List<User> findSuggestedFriends(@Param("userId") Long userId);

    @Query("SELECT u FROM User u WHERE u.id NOT IN (SELECT f.id FROM User user JOIN user.friends f WHERE user.id = :userId) AND u.id != :userId AND u.id NOT IN (SELECT fr.sender.id FROM FriendRequest fr WHERE fr.receiver.id = :userId) AND u.id NOT IN (SELECT fr.receiver.id FROM FriendRequest fr WHERE fr.sender.id = :userId)")
List<User> findSuggestedFriends(@Param("userId") Long userId);


    // @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(concat('%', :searchTerm, '%')) OR LOWER(u.firstname) LIKE LOWER(concat('%', :searchTerm, '%')) OR LOWER(u.lastname) LIKE LOWER(concat('%', :searchTerm, '%'))")
    // List<User> searchUsers(@Param("searchTerm") String searchTerm);

    
    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(concat('%', :searchTerm, '%')) OR LOWER(u.firstname) LIKE LOWER(concat('%', :searchTerm, '%')) OR LOWER(u.lastname) LIKE LOWER(concat('%', :searchTerm, '%'))")
    List<User> searchUsers(@Param("searchTerm") String searchTerm);
}
