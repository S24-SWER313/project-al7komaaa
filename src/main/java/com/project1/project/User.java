package com.project1.project;

import java.sql.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
@Entity
@Data
public class User {
    private @Id @GeneratedValue
    Long userID;
    private String username;
    private String password;
    private String email;
    private String UmlrofilePicture;
    private String bio;
    private Date dateOfBirth;
    private Date registrationDate;
    @OneToMany
    private List<Post> posts;
    @ManyToMany //مش عارف اذا لازم نفكها لجدول وسيط 
    private List<User> friends;  //احنا عاملين جدول وسيط اسمو فرندشيب ف لازم اتأكد منه 
     public User() {
    }
    public User(Long userID, String username, String password, String email, String umlrofilePicture, String bio,
            Date dateOfBirth, Date registrationDate, List<Post> posts, List<User> friends) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
        UmlrofilePicture = umlrofilePicture;
        this.bio = bio;
        this.dateOfBirth = dateOfBirth;
        this.registrationDate = registrationDate;
        this.posts = posts;
        this.friends = friends;
    }
 
    
}
