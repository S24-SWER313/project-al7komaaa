package com.project1.project.Entity.Post;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project1.project.Entity.Comment.Comment;
import com.project1.project.Entity.Like.Like;
import com.project1.project.Entity.Share.Share;
import com.project1.project.Entity.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
    private User user;
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post") //t8ayarat
    private List<Like> like;

    @OneToMany(mappedBy = "post")
    private List<Share> shares;
    public Post() {
    }

 
    public Post( String image, String video, String content, User user) {

      
        this.image = image;
        this.video = video;
        this.content = content;
        this.user = user;
      
    }


    public Post( String content, User user) {
      
        this.content = content;
        this.user = user;

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
    
}