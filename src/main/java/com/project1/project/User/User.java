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
    private String firstname;
    private String lastname;
    private String mobile;
    private String email;
    private int age;
    private String username;
    private Gender gender;
    private String bio;
    private String location;
    private String fullname;
    
    private String image;
    private LocalDate dateofbirth;
    private String backgroudimage;
    private boolean isfriend;
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
        this.firstname = firstName;
        this.lastname = lastName;
        this.mobile = mobile;
        this.email = email;
        this.gender = gender;
        this.dateofbirth = dateOfBirth;
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
        
        return firstname;
    }
    public void setFirstName(String firstName) {
        this.fullname= this.firstname = this.firstname + " " + this.lastname;
        this.firstname = firstName;
    }
    public String getLastName() {
        return lastname;
    }
    public void setLastName(String lastName) {
        this.fullname= this.firstname = this.firstname + " " + this.lastname;
        this.lastname = lastName;
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
      this.age=(int)(LocalDate.now().getYear()-dateofbirth.getYear());
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
        this.fullname= this.firstname = this.firstname + " " + this.lastname;
        return fullname;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public LocalDate getDateOfBirth() {
        return dateofbirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateofbirth = dateOfBirth;
    }
    public String getBackgroudImage() {
        return backgroudimage;
    }
    public void setBackgroudImage(String backgroudImage) {
        this.backgroudimage = backgroudImage;
    }
    public boolean isFriend() {
        return isfriend;
    }
    public void setFriend(boolean isFriend) {
        this.isfriend = isFriend;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        username = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
  