package com.project1.project.Entity.Share;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project1.project.Entity.Post.Post;

public interface ShareRepo extends JpaRepository<Share, Long> { 
    List<Share> findByPost(Post post);

}