package com.project1.project.Controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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

import com.project1.project.NFException;
import com.project1.project.Entity.Comment.Comment;
import com.project1.project.Entity.Comment.CommentModelAss;
import com.project1.project.Entity.Comment.CommentRepo;
import com.project1.project.Entity.Like.Like;
import com.project1.project.Entity.Like.LikeRepo;
import com.project1.project.Entity.Like.likeType;
import com.project1.project.Entity.Post.Post;
import com.project1.project.Entity.Post.PostModelAss;
import com.project1.project.Entity.Post.PostRepo;
import com.project1.project.Entity.Share.Share;
import com.project1.project.Entity.Share.ShareRepo;
import com.project1.project.Entity.User.User;
import com.project1.project.Entity.User.UserModelAss;
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
  private UserRepo userRepo;

  @Autowired
  private ShareRepo shareRepo;

  @Autowired
  private CommentRepo commentRepo;

  @Autowired
  private PostModelAss postmodelAss;

  @Autowired
  private UserModelAss usermodelAss;

  @Autowired
  private CommentModelAss commentmodelAss;

  @Autowired
  private LikeRepo likeRepo;

  List<EntityModel<Post>> l = new ArrayList<>();

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

  @GetMapping("/{postId}/comment")
  public ResponseEntity<?> getAllPostComments(@PathVariable Long postId, HttpServletRequest request) {

    Optional<Post> postOptional = postRepo.findById(postId);
    if (!postOptional.isPresent()) {
      MessageResponse errorMessage = new MessageResponse("Post with ID " + postId + " not found.");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    List<Comment> comments = postOptional.get().postComments;

    List<EntityModel<Comment>> commentModels = comments.stream()
        .map(com -> commentmodelAss.commentDelEdit(com, request))
        .collect(Collectors.toList());

    if (comments.isEmpty()) {
      MessageResponse noCommentsMessage = new MessageResponse("There are no comments for this post.");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noCommentsMessage);
    }
    return ResponseEntity.ok(CollectionModel.of(commentModels,
        linkTo(methodOn(PostController.class).findById(postId)).withRel("Go to Post")));

  }

  @GetMapping("/posts")
  public ResponseEntity<?> findAllPost() {
    List<EntityModel<Post>> users = postRepo.findAll().stream()
        .map(postmodelAss::toModel)
        .collect(Collectors.toList());

    if (users.isEmpty()) {
      MessageResponse errorMessage = new MessageResponse("No posts found.");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
    return ResponseEntity
        .ok(CollectionModel.of(users, linkTo(methodOn(PostController.class).findAllPost()).withRel("Go to all Posts")));

  }

  @PostMapping("/share/{postId}")
  public ResponseEntity<EntityModel<Share>> sharePost(@PathVariable Long postId, HttpServletRequest request,
                                         @RequestBody String content) {
  
      String jwt = parseJwt(request);
      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
          String username = jwtUtils.getUserNameFromJwtToken(jwt);
          User user = userRepo.findByUsername(username)
                  .orElseThrow(() -> new RuntimeException("User not found"));
          Post post = postRepo.findById(postId).orElse(null);
          if (post == null) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
          }
          Share share = new Share(content, user, post);
          shareRepo.save(share);
         // EntityModel<Share> entityModel = EntityModel.of();
          return ResponseEntity.ok(postmodelAss.toModelsharepostId(share, request));
      }
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); 
  }
  
  @GetMapping("/posts/{postId}")
  public ResponseEntity<EntityModel<Post>> findById(@PathVariable Long postId) {
    Post post = postRepo.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
    //
    return ResponseEntity.ok(postmodelAss.toModelpostId(post));

  }

  //
  @DeleteMapping("/{postId}")
    public ResponseEntity<?> deleteById(@PathVariable Long postId, HttpServletRequest request) {
        String jwt = parseJwt(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
          String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepo.findByUsername(username)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                       Optional<Post> post= postRepo.findById(postId);
                       if(!post.isPresent()){
                        return ResponseEntity.badRequest().body("post not found");}

        else{
          if (userHasPermissionToDeletePost(postId,user.getId())) {
          postRepo.deleteById(postId);
    return ResponseEntity.ok().body("the post is deleted successfully");
         
       
        }else {
           return  ResponseEntity.badRequest().body("you are not owned the post");
        }}}

        return  ResponseEntity.badRequest().body("you are not auth");
        
    
  }

  /////////////////////////////////////////////////////////////////////
  @PostMapping("/comment/{postId}/post")
  public ResponseEntity<?> createComment(HttpServletRequest request, @RequestBody Comment comment,
      @PathVariable Long postId) {

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
      userRepo.save(user);
      postRepo.save(post);
      
      commentRepo.save(comment);
    }
    return ResponseEntity.ok(commentmodelAss.commentDelEdit(comment,request));

  }

  @DeleteMapping("/comment/{commentId}")
  public ResponseEntity deleteComment(@PathVariable Long commentId, HttpServletRequest request) {
    String jwt = parseJwt(request);
    if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
      String username = jwtUtils.getUserNameFromJwtToken(jwt);
      User user = userRepo.findByUsername(username)
          .orElseThrow(() -> new RuntimeException("User not found"));
      // List<Comment>postComments=postRepo.postComments(postId);
      Comment comment = commentRepo.findById(commentId).get();
      User userComment = comment.getUser();

      if (userHasPermissionToDeleteComment(commentId, user.getId())) {
        commentRepo.deleteById(commentId);
      } else {
        return ResponseEntity.ok(new MessageResponse("you are not authorized!"));
      }
      return ResponseEntity.ok(new MessageResponse("Delete successfully!"));
    }
    return ResponseEntity.ok(new MessageResponse("you are not authenticated!"));
  }

  private boolean userHasPermissionToDeleteComment(Long commentId, Long userId) {

    Comment comment = commentRepo.findById(commentId).orElse(null);

    return comment != null && comment.getUser().getId().equals(userId);
  }

  ////////////////////////////////////////////////////////////////////
  @GetMapping("/user/like")
  public ResponseEntity<CollectionModel<EntityModel<Post>>> findByLikesContainsUser(HttpServletRequest request) {// البوستات
                                                                                                                 // الي
                                                                                                                 // اليوزر
                                                                                                                 // حاط
                                                                                                                 // لايك
                                                                                                                 // عليهم
    User user = null;
    String jwt = parseJwt(request);
    if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
      String username = jwtUtils.getUserNameFromJwtToken(jwt);
      user = userRepo.findByUsername(username)
          .orElseThrow(() -> new RuntimeException("User not found"));
      List<Like> likeList = likeRepo.findByUser(user);
      List<Post> likePost = likeList.stream().map(e -> e.post).collect(Collectors.toList());
      ///////////////////
      List<EntityModel<Post>> users = likePost.stream()
          .map(e -> postmodelAss.toModelpostId(e))
          .collect(Collectors.toList());

      if (users.isEmpty()) {
        throw new NFException(User.class);
      }
      return ResponseEntity.ok(CollectionModel.of(users,
          linkTo(methodOn(Controller.class).getUserById(user.getId())).withRel("User Profile")));

    }

    return ResponseEntity.ok(
        CollectionModel.of(l, linkTo(methodOn(Controller.class).getUserById(user.getId())).withRel("User Profile")));
  }

  // @PostMapping("/{postId}/like")
  // public Like CreatelikePost(@RequestBody Like like,@PathVariable Long
  // postId,HttpServletRequest request) {// يوزر يوزر مش حاسها زابطة
  // String jwt = parseJwt(request);
  // if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
  // String username = jwtUtils.getUserNameFromJwtToken(jwt);
  // User user = userRepo.findByUsername(username)
  // .orElseThrow(() -> new RuntimeException("User not found"));
  // like.setUser(user);
  // Post post= postRepo.findById(postId).get();
  // like.setPost(post);
  // post.like.add(like);
  // postRepo.save(post);
  // userRepo.save(user);
  // return likeRepo.save(like);}

  // return new Like();

  // }

  @PostMapping("/{postId}/like")
  public ResponseEntity<?> createLikePost(@RequestBody Like like, @PathVariable Long postId,
      HttpServletRequest request) {
    String jwt = parseJwt(request);
    if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
      String username = jwtUtils.getUserNameFromJwtToken(jwt);

      User user = userRepo.findByUsername(username).orElse(null);
      if (user == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
      }

      Post post = postRepo.findById(postId).orElse(null);
      if (post == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
      }

      like.setUser(user);
      like.setPost(post);

      if (post.like.stream().anyMatch(l -> l.user.getId().equals(user.getId()))) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User has already liked the post");
      }

      Like savedLike = likeRepo.save(like);

      user.likes.add(savedLike);
      post.like.add(savedLike);

      userRepo.save(user);
      postRepo.save(post);

      return ResponseEntity.status(HttpStatus.CREATED).body(savedLike);
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing JWT token");
    }
  }

  @GetMapping("/{postId}/likes")
  public ResponseEntity<List<EntityModel<Like>>> getAllPostLikes(@PathVariable Long postId) {
    Optional<Post> postOptional = postRepo.findById(postId);

    if (!postOptional.isPresent()) {
      return ResponseEntity.notFound().build();
    }
    List<Like> likes = postRepo.findLikes(postId);

    List<EntityModel<Like>> entityModels = likes.stream()
        .map(us -> postmodelAss.toModelPostLike(us))
        .collect(Collectors.toList());

    return ResponseEntity.ok(entityModels);
  }

  @DeleteMapping("/{likeId}/like")
  public ResponseEntity<MessageResponse> UnCreatelikePost(@PathVariable Long likeId) {
    likeRepo.deleteById(likeId);
    return ResponseEntity.ok(new MessageResponse("Delete successfully!"));
  }

  // @PostMapping("/{commentId}/comment")
  // public Like commentLike(@PathVariable Long commentId,@RequestBody Like
  // like,@RequestHeader("Authorization") String jwt) {// اذا في لايك بشيله اذا فش
  // بحط
  // String username = jwtUtils.extractUsername(jwt);
  // User user = userRepo.findByUsername(username)
  // .orElseThrow(() -> new RuntimeException("User not found"));
  // Comment comment= commentRepo.findById(commentId).get();
  // comment.commentLike.add(like);
  // user.likes.add (like);
  // like.setComment(comment);
  // like.setUser(user);
  // return likeRepo.save(like);

  // }

  // public List<Like> getAllCommentLike(Long PostId) {
  // return null;

  // }

  //////////////////////////////////////////////////////
  // public Post creatShare(Long postId, Long userId, String content) {// بصير
  ////////////////////////////////////////////////////// كانه بوست بوخذ اي دي جديد
  ////////////////////////////////////////////////////// ;الكونتينت بكون
  // // ب البادي
  // return null;

  // }
  @DeleteMapping(("/share/{shareId}"))
  public ResponseEntity<MessageResponse> deleteShearById(@PathVariable Long shareId, HttpServletRequest request)  {

    String jwt = parseJwt(request);
    if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
      String username = jwtUtils.getUserNameFromJwtToken(jwt);
      User user = userRepo.findByUsername(username)
          .orElseThrow(() -> new RuntimeException("User not found"));
      Share share = shareRepo.findById(shareId).get();
      if (userHasPermissionToDeletePost(share.user.getId(), user.getId())) {
        shareRepo.deleteById(shareId);
        return ResponseEntity.ok(new MessageResponse("delete share successfully"));
      } else {
        return ResponseEntity.ok(new MessageResponse("User is not authorized to delete this post"));
      }
    }
    return ResponseEntity.ok(new MessageResponse("you are not authorized!"));

  }

  @GetMapping("/userPosts")
  public ResponseEntity<CollectionModel<EntityModel<Post>>> findUserPosts(HttpServletRequest request) {
    User user = null;
    String jwt = parseJwt(request);
    if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
      String username = jwtUtils.getUserNameFromJwtToken(jwt);
      user = userRepo.findByUsername(username).get();
      List<EntityModel<Post>> users = userRepo.findPostsByUserId(user.getId()).stream()
          .map(e -> postmodelAss.toModelpostId(e))
          .collect(Collectors.toList());

      if (users.isEmpty()) {
        throw new NFException(User.class);
      }
      return ResponseEntity.ok(CollectionModel.of(users,
          linkTo(methodOn(PostController.class).findById(user.getId())).withRel("Go to Post")));
    }

    return ResponseEntity.ok(
        CollectionModel.of(l, linkTo(methodOn(Controller.class).getUserById(user.getId())).withRel("User Profile")));
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

  @GetMapping("/FriendPosts/{friendId}")
  public ResponseEntity<?> findFriendPosts(HttpServletRequest request, @PathVariable Long friendId) {
    String jwt = parseJwt(request);
    if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
      String username = jwtUtils.getUserNameFromJwtToken(jwt);
      Optional<User> optionalUser = userRepo.findByUsername(username);
      Optional<User> optionalFriend = userRepo.findById(friendId);
      if (optionalUser.isPresent() && optionalFriend.isPresent()) {
        User user = optionalUser.get();
        User friend = optionalFriend.get();
        boolean isFriend = user.friends.contains(friend);

        if (isFriend) {
          List<Post> friendPosts = userRepo.findPostsByUserId(friendId);
          List<EntityModel<Post>> users = postRepo.findAll().stream()
          .map(postmodelAss::toModel)
          .collect(Collectors.toList());
  
      if (users.isEmpty()) {
        MessageResponse errorMessage = new MessageResponse("No posts found.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
      }
      return ResponseEntity
          .ok(CollectionModel.of(users, linkTo(methodOn(PostController.class).findAllPost()).withRel("Go to all Posts")));
   
       //   return ResponseEntity.ok(postmodelAss.toModelpostId(friendPosts));
        } else {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body("This acount is private to see posts added friend.");
        }
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or friend not found.");
      }
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access. sign in");
    }
  }
  

  @GetMapping("/postUser/{postid}")
  public ResponseEntity<EntityModel<User>> postUser(@PathVariable Long postid) {
    Post post = postRepo.findById(postid).get();

    return ResponseEntity.ok(usermodelAss.toModeluserprofile(post.user));

  }

}