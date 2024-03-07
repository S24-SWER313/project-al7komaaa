

package com.project1.project.User;
import jakarta.persistence.*;


import java.util.List;

import com.project1.project.Comment.Comment;
import com.project1.project.Like.Like;
import com.project1.project.Post.Post;
import com.project1.project.Share.Share;

@Entity

@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private int age;
    private Gender gender;
    private String bio;
    private String live;


    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    private List<Like> likes;

    @OneToMany(mappedBy = "user")
    private List<Share> shares;

  
    @ManyToMany
    @JoinTable(
        name = "user_friends",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<User> friends;

   // public User(Long id, String firstName, String lastName, String phone, String email, int age, Gender gender,
    //         String bio, String live) {
    //     this.id = id;
    //     this.firstName = firstName;
    //     this.lastName = lastName;
    //     this.phone = phone;
    //     this.email = email;
    //     this.age = age;
    //     this.gender = gender;
    //     this.bio = bio;
    //     this.live = live;

    // }

    public User() {
    }

    public User(Long id, String firstName, String lastName, String phone, String email, int age, Gender gender,
            String bio, String live) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.bio = bio;
        this.live = live;
    }
   public String getName(){
    return this.firstName+" "+this.lastName;
   }


   public void addFriend(User friend){

Long count = friends.stream().filter(e->e.getId().equals(friend.getId())).count();
    if(count == 0)
    friends.add(friend);
   }

@Override
public String toString() {
    return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", phone=" + phone + ", email="
            + email + ", age=" + age + ", gender=" + gender + ", bio=" + bio + ", live=" + live + "]";
}

public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public String getFirstName() {
    return firstName;
}

public void setFirstName(String firstName) {
    this.firstName = firstName;
}

public String getLastName() {
    return lastName;
}

public void setLastName(String lastName) {
    this.lastName = lastName;
}

public String getPhone() {
    return phone;
}

public void setPhone(String phone) {
    this.phone = phone;
}

public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}

public int getAge() {
    return age;
}

public void setAge(int age) {
    this.age = age;
}

public Gender getGender() {
    return gender;
}

public void setGender(Gender gender) {
    this.gender = gender;
}

public String getBio() {
    return bio;
}

public void setBio(String bio) {
    this.bio = bio;
}

public String getLive() {
    return live;
}

public void setLive(String live) {
    this.live = live;
}
    
   
}
