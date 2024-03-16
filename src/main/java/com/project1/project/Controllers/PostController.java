package com.project1.project.Controllers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project1.project.Entity.Comment.Comment;
import com.project1.project.Entity.Comment.CommentRepo;
import com.project1.project.Entity.Like.Like;
import com.project1.project.Entity.Like.likeType;
import com.project1.project.Entity.Post.Post;
import com.project1.project.Entity.Post.PostRepo;
import com.project1.project.Entity.Share.Share;
import com.project1.project.Entity.Share.ShareRepo;
import com.project1.project.Entity.User.User;
import com.project1.project.Entity.User.UserRepo;
import com.project1.project.Payload.Response.MessageResponse;
import com.project1.project.Security.Jwt.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/post")
public class PostController {
  @Autowired
  private JwtUtils jwtUtils;
 
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private  UserRepo userRepo;

    @Autowired
    private  ShareRepo shareRepo;

    @Autowired
    private  CommentRepo commentRepo;

   
    
    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestHeader("Authorization") String jwt, @RequestBody Post post) {
      
        String username = jwtUtils.extractUsername(jwt);
        
        User user = userRepo.findByUsername(username)
                            .orElseThrow(() -> new RuntimeException("User not found"));
        
        post.setUser(user);
        
        postRepo.save(post);
        
        return ResponseEntity.ok(new MessageResponse("Post Created successfully!"));
    }
    



    @GetMapping("/posts")
    public List<Post> findAllPost() {

        return postRepo.findAll();
    }

    @PostMapping("/share/{postId}")
    public ResponseEntity<?> sharePost(@PathVariable Long postId, @RequestHeader("Authorization") String jwt, @RequestBody String content) {
      
            String username = jwtUtils.extractUsername(jwt);
            User user = userRepo.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("User not found"));
            Post post = postRepo.findById(postId).orElse(null);
            if (post == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
            }
            Share share = new Share(content, user, post);
            shareRepo.save(share);
            return ResponseEntity.ok(new MessageResponse("Share created successfully!"));
    }
    

    @GetMapping("/posts/{postId}")
    public Post findById(@PathVariable Long postId) {
        return postRepo.findById(postId).get();
        // .orElseThrow(new NFException(Post.class));

    }

    @DeleteMapping("/{postId}")
    public void deleteById(@PathVariable Long postId, @RequestHeader("Authorization") String jwt) throws Exception {
        String username = jwtUtils.extractUsername(jwt);
        User user = userRepo.findByUsername(username)
                            .orElseThrow(() -> new RuntimeException("User not found"));
        if (userHasPermissionToDeletePost(postId, user.getId())) {
    
            postRepo.deleteById(postId);
        } else {
            throw new Exception("User is not authorized to delete this post");
        }
    }
    
    private boolean userHasPermissionToDeletePost(Long postId, Long userId) {
     
        Post post = postRepo.findById(postId).orElse(null);

        return post != null && post.user.getId().equals(userId);
    }
    
    /////////////////////////////////////////////////////////////////////
    @PostMapping("/comment/{postId}/post")
    public ResponseEntity<MessageResponse> createComment( @RequestHeader("Authorization") String jwt, @RequestBody Comment comment, @PathVariable Long postId) {

        String username = jwtUtils.extractUsername(jwt);
        User user = userRepo.findByUsername(username)
                            .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepo.findById(postId).orElse(null);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Post not found"));
        }
        post.postComments.add(comment);
        comment.setUser(user);
        comment.setPost(post);
        // user.comments.add(comment);
        commentRepo.save(comment);
        return ResponseEntity.ok(new MessageResponse("Share created successfully!"));
       
        
    }
 
   @DeleteMapping("/comment/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long commentId, @RequestHeader("Authorization") String jwt)  {
        String username = jwtUtils.extractUsername(jwt);
        User user = userRepo.findByUsername(username)
                            .orElseThrow(() -> new RuntimeException("User not found"));
    //   List<Comment>postComments=postRepo.postComments(postId);
      Comment comment=commentRepo.findById(commentId).get();
      User userComment=comment.getUser();

      if (userHasPermissionToDeleteComment(commentId, user.getId())) {
        commentRepo.deleteById(commentId);
    } else {
        return ResponseEntity.ok(new MessageResponse("you are not authorized!"));
    }
        return ResponseEntity.ok(new MessageResponse("Delete successfully!"));

    }
    private boolean userHasPermissionToDeleteComment(Long commentId, Long userId) {
        // قم بفحص قاعدة البيانات أو أي نظام مصادقة آخر للتحقق مما إذا كان المستخدم هو صاحب المنشور
        // على سبيل المثال، يمكنك التحقق من أن المستخدم مملوك للمنشور في قاعدة البيانات
        Comment comment = commentRepo.findById(commentId).orElse(null);

        return comment != null && comment.getUser().getId().equals(userId);
    }

    ////////////////////////////////////////////////////////////////////
    public List<Post> findByLikesContainsUser(User user) {// البوستات الي اليوزر حاط لايك عليهم
        return null;
    }

    public Like likePost(Long postId, User user) {// يوزر يوزر مش حاسها زابطة
        return null;

    }

    public List<Like> getAllPostLike(Long PostId) {
        return null;

    }

    // @GetMapping("/posts/{id}/like")
    // public List<Like> allPostLike(@PathVariable Long id){
    // Post p= postRepo.findById(id).get();
    // return postRepo.findByLike(p);
    // }

    public Post postLike(Long postId, likeType type) {// اذا في لايك بشيله اذا فش بحط
        return null;

    }

    public Comment commentLike(Long postId, Long commentId, likeType type) {// اذا في لايك بشيله اذا فش بحط
        return null;

    }

    public List<Like> getAllCommentLike(Long PostId) {
        return null;

    }

    //////////////////////////////////////////////////////
    public Post creatShare(Long postId, Long userId, String content) {// بصير كانه بوست بوخذ اي دي جديد ;الكونتينت بكون
                                                                      // ب البادي
        return null;

    }

    public void deleteShearById(Long shearId, Long userId) {
    }



    @GetMapping("/userPosts")
public List<Post> findUserPosts(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId"); 
    if (userId == null) {
        return Collections.emptyList();
    }
    User user = userRepo.findById(userId).orElse(null);
    if (user == null) {
        return Collections.emptyList();
    }
    return user.posts; 
}}