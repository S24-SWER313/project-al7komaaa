package com.project1.project.Entity.Post;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project1.project.Entity.Comment.Comment;
import com.project1.project.Entity.Like.Like;
import com.project1.project.Entity.Share.Share;
import com.project1.project.Entity.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
// private Type type;
private String image;
private String video;
@Size(max = 1000000, message = "Content is too long")
private String content;
// private Long count =(long) 1;


  @JsonIgnore
  @ManyToOne 
    @JoinColumn(name = "user_id")
    public User user;
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    public List<Comment> postComments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL) //t8ayarat
    public List<Like> like;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Share> shares;
    public Post() {
    }



    public Post( String content) {
      
        this.content = content;
     

    }
  

    public Long getPostId() {
        return postId;
    }
    public void setPostId(Long postId) {
        this.postId = postId;
    }
  public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
  
    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    

    // public byte[] getVideoReel() {
    //     try {
    //         Path videoFile = Paths.get(getVideo());
    //         byte[] videoBytes = Files.readAllBytes(videoFile);
    //         return videoBytes;
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         return null; 
    //     }
    // }
    }