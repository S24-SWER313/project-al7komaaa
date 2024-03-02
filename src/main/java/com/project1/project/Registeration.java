package com.project1.project;

import java.util.Date;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Registeration {
    private @Id @GeneratedValue
     Long registrationID;
     @OneToOne
    private Long userID;
    private Date registrationDate;
    public Registeration(Long registrationID, Long userID, Date registrationDate) {
        this.registrationID = registrationID;
        this.userID = userID;
        this.registrationDate = registrationDate;
    }
    public Registeration() {
    }

    
    
}
