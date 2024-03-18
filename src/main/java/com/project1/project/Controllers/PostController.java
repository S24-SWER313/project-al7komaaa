package com.project1.project.Controllers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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
import com.project1.project.Entity.Like.LikeRepo;
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

    @Autowired
    private  LikeRepo likeRepo;
    
    @PostMapping("/create")
    public ResponseEntity<?> createPost(HttpServletRequest request, @RequestBody Post post) {
        // @RequestHeader("Authorization") String jwt,
        // String username = jwtUtils.extractUsername(jwt);
        String jwt = parseJwt(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
          String username = jwtUtils.getUserNameFromJwtToken(jwt);
        
        
        User user = userRepo.findByUsername(username)
                            .orElseThrow(() -> new RuntimeException("User not found"));
        
        post.setUser(user);
        
        postRepo.save(post);
        }
        return ResponseEntity.ok(new MessageResponse("Post Created successfully!"));
    }
    



    @GetMapping("/posts")
    public List<Post> findAllPost() {

        return postRepo.findAll();
    }

    @PostMapping("/share/{postId}")
    public ResponseEntity<?> sharePost(@PathVariable Long postId, HttpServletRequest request, @RequestBody String content) {
      
        String jwt = parseJwt(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
          String username = jwtUtils.getUserNameFromJwtToken(jwt);
            User user = userRepo.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("User not found"));
            Post post = postRepo.findById(postId).orElse(null);
            if (post == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
            }
            Share share = new Share(content, user, post);
            shareRepo.save(share);}
            return ResponseEntity.ok(new MessageResponse("Share created successfully!"));
    }
    

    @GetMapping("/posts/{postId}")
    public Post findById(@PathVariable Long postId) {
        return postRepo.findById(postId).get();
        // .orElseThrow(new NFException(Post.class));

    }

    @DeleteMapping("/{postId}")
    public void deleteById(@PathVariable Long postId, HttpServletRequest request) throws Exception {
        String jwt = parseJwt(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
          String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepo.findByUsername(username)
                            .orElseThrow(() -> new RuntimeException("User not found"));
        if (userHasPermissionToDeletePost(postId, user.getId())) {
    
            postRepo.deleteById(postId);
        } else {
            throw new Exception("User is not authorized to delete this post");
        }}
    }
    
   
    
    /////////////////////////////////////////////////////////////////////
    @PostMapping("/comment/{postId}/post")
    public ResponseEntity<MessageResponse> createComment( HttpServletRequest request, @RequestBody Comment comment, @PathVariable Long postId) {

        String jwt = parseJwt(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
          String username = jwtUtils.getUserNameFromJwtToken(jwt);
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
        commentRepo.save(comment);}
        return ResponseEntity.ok(new MessageResponse("Share created successfully!"));
       
        
    }
 
   @DeleteMapping("/comment/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long commentId, HttpServletRequest request)  {
        String jwt = parseJwt(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
          String username = jwtUtils.getUserNameFromJwtToken(jwt);
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
        }return null;
    }
    private boolean userHasPermissionToDeleteComment(Long commentId, Long userId) {
        
        Comment comment = commentRepo.findById(commentId).orElse(null);

        return comment != null && comment.getUser().getId().equals(userId);
    }

    ////////////////////////////////////////////////////////////////////
    @GetMapping("/user/like")
    public List<Post> findByLikesContainsUser( HttpServletRequest request) {// البوستات الي اليوزر حاط لايك عليهم
        String jwt = parseJwt(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
          String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepo.findByUsername(username)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                          List<Like>likeList=likeRepo.findByUser(user);
                    List<Post> likePost=likeList.stream().map(e->e.post).collect(Collectors.toList());
                    return likePost;}
                    return null;
    }
    @PostMapping("/{postId}/like")
    public Like CreatelikePost(@RequestBody Like like,@PathVariable Long postId,HttpServletRequest request) {// يوزر يوزر مش حاسها زابطة
        String jwt = parseJwt(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
          String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepo.findByUsername(username)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                            like.setUser(user);
                        Post post=    postRepo.findById(postId).get();
                            like.setPost(post);
                        return likeRepo.save(like);}
        return null;

    }
    @GetMapping("/{postId}/likes")
    public List<Like> getAllPostLike(@PathVariable Long postId) {
      
        return postRepo.findLikes(postId);
}

    
@DeleteMapping("/{likeId}/like")
public ResponseEntity<MessageResponse> UnCreatelikePost(@PathVariable Long likeId) {
    likeRepo.deleteById(likeId);
      return ResponseEntity.ok(new MessageResponse("Delete successfully!"));}



    // @PostMapping("/{commentId}/comment")
    // public Like commentLike(@PathVariable Long commentId,@RequestBody Like like,@RequestHeader("Authorization") String jwt) {// اذا في لايك بشيله اذا فش بحط
    //     String username = jwtUtils.extractUsername(jwt);
    //     User user = userRepo.findByUsername(username)
    //                         .orElseThrow(() -> new RuntimeException("User not found"));
    //         Comment comment= commentRepo.findById(commentId).get();
    //         comment.commentLike.add(like);
    //        user.likes.add (like);
    //         like.setComment(comment);
    //         like.setUser(user);
    //         return likeRepo.save(like);

    // }

    // public List<Like> getAllCommentLike(Long PostId) {
    //     return null;

    // }

    //////////////////////////////////////////////////////
    // public Post creatShare(Long postId, Long userId, String content) {// بصير كانه بوست بوخذ اي دي جديد ;الكونتينت بكون
    //                                                                   // ب البادي
    //     return null;

    // }
@DeleteMapping(("/share/{shareId}"))
    public void deleteShearById(@PathVariable Long shareId,  HttpServletRequest request) throws Exception {
        
        String jwt = parseJwt(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
          String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepo.findByUsername(username)
                            .orElseThrow(() -> new RuntimeException("User not found"));

                            if (userHasPermissionToDeletePost(shareId, user.getId())) {
                          shareRepo.deleteById(shareId);
                            } else {
                                throw new Exception("User is not authorized to delete this post");
                            }}
                        


    }



    @GetMapping("/userPosts")
    public List<Post> findUserPosts(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId"); 
        if (userId == null) {
            return Collections.emptyList();
        }
        // Use the corrected method to fetch user's posts
        return userRepo.findPostsByUserId(userId);
    }
    
private boolean userHasPermissionToDeletePost(Long postId, Long userId) {
     
        Post post = postRepo.findById(postId).orElse(null);

        return post != null && post.user.getId().equals(userId);
    }



 private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7);
    }

    return null;
  }

}