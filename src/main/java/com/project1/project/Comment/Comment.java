package com.project1.project.Comment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project1.project.Post.Post;
// import com.project1.project.Post.Type;
import com.project1.project.User.User;
import jakarta.persistence.*;
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    // private Type type;
    private String content;
    private String image;
    private String video;
    private static Long count = 0L; // متغير ثابت لزيادة العداد
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    public Comment() {
    }

    public Comment( String content, String image, String video, User user, Post post) {
        this.commentId =++count;
        this.content = content;
        this.image = image;
        this.video = video;
        this.user = user;
        this.post = post;
    }
    public Comment(String content, User user, Post post) {
        this.commentId =++count;
        this.content = content;
        this.user = user;
        this.post = post;
    }
    public Long getCommentId() {
        return commentId;
    }
    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Post getPost() {
        return post;
    }
    public void setPost(Post post) {
        this.post = post;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
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
}