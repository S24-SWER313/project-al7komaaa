package com.project1.project.Entity.Like;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.project1.project.Entity.User.User;


public interface LikeRepo extends JpaRepository<Like, Long> { 

List<Like> findByUser(User user);
}