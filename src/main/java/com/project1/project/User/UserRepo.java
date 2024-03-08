package com.project1.project.User;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepo extends JpaRepository<User, Long> { 

    List<User> findByFirstName(String firstName);

    List< User> findByLastName(String lastName);
    // List <User> findByGetName(String name);
 @Query("SELECT u FROM User u WHERE CONCAT(u.firstName, ' ', u.lastName) = ?1")
    List<User> findByFullName(String fullName);
}