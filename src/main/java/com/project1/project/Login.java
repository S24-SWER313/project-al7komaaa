package com.project1.project;


import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Login {
    private @Id @GeneratedValue
     Long loginID;
     @ManyToOne
    private Long userID;
    public Login(Long loginID, Long userID) {
        this.loginID = loginID;
        this.userID = userID;
    }
    public Login() {
    }
    
    
    
}
