package com.project1.project;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepo extends JpaRepository<Story, Long>{
    
}
