package com.project1.project.User;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> { 

    List<User> findByFirstName(String firstName);

    List<User> findByLastName(String lastName);
    Optional<User> findByName(String name);

}