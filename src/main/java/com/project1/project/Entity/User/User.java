package com.project1.project.Entity.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.project1.project.Entity.Comment.Comment;
import com.project1.project.Entity.Like.Like;
import com.project1.project.Entity.Post.Post;
import com.project1.project.Entity.Share.Share;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
   @NotNull(message = "First name is required")
   @Pattern(regexp = "^[a-zA-Z]*$", message = "First name must contain only letters")
    @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters")
    private String firstname;

      
   @NotNull(message = "Last name is required")
   @Pattern(regexp = "^[a-zA-Z]*$", message = "Last name must contain only letters")
    @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters")
    private String lastname;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp="\\d+", message="Mobile number must contain only digits")
    private String mobile;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    //  @Min(value = 18, message = "Age must be at least 18")
    // @Max(value = 120, message = "Age must be less than or equal to 120")
    private int age;

    
    private String username;
    private Gender gender;
    private String bio;
    private String location;

    
    private String fullname;
    private String image;
    
    @Past(message = "Date of birth must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateofbirth;

    private String backgroudimage;
    private boolean isfriend;
    private String password;

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

    public User(String firstname, String lastname, String mobile, String email, Gender gender,
            LocalDate dateofbirth) {

        this.firstname = firstname;
        this.lastname = lastname;
        this.mobile = mobile;
        this.email = email;
        this.gender = gender;
        this.dateofbirth = dateofbirth;
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

    public String getFirstname() {

        return firstname;
    }

    public void setFirstname(String firstName) {
        this.fullname = this.firstname = this.firstname + " " + this.lastname;
        this.firstname = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastName) {
        this.fullname = this.firstname = this.firstname + " " + this.lastname;
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
        if (dateofbirth == null) {
            return 0;
        } else {
            this.age = (int) (LocalDate.now().getYear() - getDateofbirth().getYear());
           
            return age;
        }
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

    public String getFullname() {
        this.fullname = this.firstname = this.firstname + " " + this.lastname;
        return fullname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDate getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(LocalDate dateOfBirth) {
        this.dateofbirth = dateOfBirth;
    }

    public String getBackgroudimage() {
        return backgroudimage;
    }

    public void setBackgroudimage(String backgroudImage) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        username = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

}