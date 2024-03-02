package com.project1.project;

import java.util.Date;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Friendship {
private @Id @GeneratedValue
     Long friendshipID;
     @OneToMany
    private Long userID1;
    @ManyToOne
    private Long userID2;
    private Date friendshipDate;
    public Friendship(Long friendshipID, Long userID1, Long userID2, Date friendshipDate) {
        this.friendshipID = friendshipID;
        this.userID1 = userID1;
        this.userID2 = userID2;
        this.friendshipDate = friendshipDate;
    }
    public Friendship() {
    }
    
    
}
