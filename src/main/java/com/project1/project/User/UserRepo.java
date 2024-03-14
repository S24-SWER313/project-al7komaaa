package com.project1.project.User;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepo extends JpaRepository<User, Long> { 

    List<User> findByFirstName(String firstName);

    List< User> findByLastName(String lastName);
    // List <User> findByGetName(String name);
 @Query("SELECT u FROM User u WHERE CONCAT(u.firstName, ' ', u.lastName) = ?1")
    List<User> findByFullName(String fullName);


    Optional<User> findByUsername(String username);

    Optional<User> findByUserName(String userName);

    Boolean existsByEmail(String email);
  
}