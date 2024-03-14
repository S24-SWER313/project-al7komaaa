package com.project1.project.User;

import jakarta.persistence.*;

import java.time.LocalDate;
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
    private String mobile;
    private String email;
    private int age;
    private String UserName;
    private Gender gender;
    private String bio;
    private String location;
    private String fullName;
    private String image;
    private LocalDate dateOfBirth;
    private String backgroudImage;
    private boolean isFriend;
private String password ;

    private static Long count = 0L; // متغير ثابت لزيادة العداد
    @OneToMany(mappedBy = "user")
    private List<Post> posts;
    @OneToMany(mappedBy = "user")
    private List<Comment> comments;
    @OneToMany(mappedBy = "user")
    private List<Like> likes;
    @OneToMany(mappedBy = "user")
    private List<Share> shares;
    @ManyToMany
    @JoinTable(name = "user_friends", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private List<User> friends;
    
    public User() {
    }
   
    public User( String firstName, String lastName, String mobile, String email,  Gender gender,
            LocalDate dateOfBirth) {


        this.id =++count;
        this.count=count++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }
    public void addFriend(User friend) {
        Long count = friends.stream().filter(e -> e.getId().equals(friend.getId())).count();
        if (count == 0)
            friends.add(friend);
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
        this.fullName= this.firstName = this.firstName + " " + this.lastName;
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.fullName= this.firstName = this.firstName + " " + this.lastName;
        this.lastName = lastName;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getAge() {
      this.age=(int)(LocalDate.now().getYear()-dateOfBirth.getYear());
               return age;
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
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getFullName() {
        this.fullName= this.firstName = this.firstName + " " + this.lastName;
        return fullName;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getBackgroudImage() {
        return backgroudImage;
    }
    public void setBackgroudImage(String backgroudImage) {
        this.backgroudImage = backgroudImage;
    }
    public boolean isFriend() {
        return isFriend;
    }
    public void setFriend(boolean isFriend) {
        this.isFriend = isFriend;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
  