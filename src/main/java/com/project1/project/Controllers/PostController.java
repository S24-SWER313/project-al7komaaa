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
import org.springframework.web.bind.annotation.PutMapping;
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
  HttpServletRequest request;
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
  public ResponseEntity<?> createPost( @RequestBody Post post) {
     User user = userFromToken(request);
        if (user==null)
return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      post.setUser(user);
      postRepo.save(post);
      return ResponseEntity.ok(new MessageResponse("Post Created successfully!"));
  }

  @GetMapping("/{postId}/comment")
  public ResponseEntity<?> getAllPostComments(@PathVariable Long postId) {

    Optional<Post> postOptional = postRepo.findById(postId);
    if (!postOptional.isPresent()) {
      MessageResponse errorMessage = new MessageResponse("Post with ID " + postId + " not found.");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    List<Comment> comments = postOptional.get().postComments;

    List<EntityModel<Comment>> commentModels = comments.stream()
        .map(com -> commentmodelAss.commentDelEdit(com))
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

        User user = userFromToken(request);
        if (user==null)
return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    List<EntityModel<Post>> users = postRepo.findAll().stream().filter(e->e.getUser().getAccountIsPrivate()==false||user.friends.contains(e.getUser()))
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
  public ResponseEntity<?> sharePost(@PathVariable Long postId,
                                         @RequestBody String content) {
  
                                          User user = userFromToken(request);
                                          if (user==null)
                                  return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
          Post post = postRepo.findById(postId).orElse(null);
          if (post == null) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
          }
          if(!post.getUser().getAccountIsPrivate()||post.getUser().equals(user)){
          Share share = new Share(content, user, post);
          shareRepo.save(share);
         // EntityModel<Share> entityModel = EntityModel.of();
          return ResponseEntity.ok(postmodelAss.toModelsharepostId(share));}else{
            return ResponseEntity.badRequest().body("this post is private");
          }
  
  }
  
  @GetMapping("/posts/{postId}")
  public ResponseEntity<?> findById(@PathVariable Long postId) {
    Post post = postRepo.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
    User user = userFromToken(request);
        if (user==null)
return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
if(!post.getUser().getAccountIsPrivate()||user.friends.contains(post.getUser())||post.getUser().equals(user))
return ResponseEntity.ok(postmodelAss.toModelpostId(post)); 
return ResponseEntity.badRequest().body("this post is private");
  }

  //
  @DeleteMapping("/{postId}")
    public ResponseEntity<?> deleteById(@PathVariable Long postId) {
      User user = userFromToken(request);
          if (user==null)
  return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                       Optional<Post> post= postRepo.findById(postId);
                       if(!post.isPresent()){
                        return ResponseEntity.badRequest().body("post not found");}
        else{
          if (userHasPermissionToDeletePost(postId,user.getId())) {
          postRepo.deleteById(postId);
    return ResponseEntity.ok().body("the post is deleted successfully");
       }else {
           return  ResponseEntity.badRequest().body("you are not owned the post");
        }}
  }

  /////////////////////////////////////////////////////////////////////
  @PostMapping("/comment/{postId}/post")
  public ResponseEntity<?> createComment( @RequestBody Comment comment,
      @PathVariable Long postId) {
        User user = userFromToken(request);
        if (user==null)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
      return ResponseEntity.ok(commentmodelAss.commentDelEdit(comment));
}

  @DeleteMapping("/comment/{commentId}")
  public ResponseEntity deleteComment(@PathVariable Long commentId) {
    User user = userFromToken(request);
        if (user==null)
return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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

  private boolean userHasPermissionToDeleteComment(Long commentId, Long userId) {

    Comment comment = commentRepo.findById(commentId).orElse(null);

    return comment != null && comment.getUser().getId().equals(userId);
  }

  ////////////////////////////////////////////////////////////////////
  @GetMapping("/user/like")
  public ResponseEntity<CollectionModel<EntityModel<Post>>> findByLikesContainsUser() {
                                                                                                             
    User user = userFromToken(request);
    if (user==null)
return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
  public ResponseEntity<?> createLikePost(@RequestBody Like like, @PathVariable Long postId) {
    User user = userFromToken(request);
    if (user==null)
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
  public ResponseEntity<MessageResponse> deleteShearById(@PathVariable Long shareId)  {
    User user = userFromToken(request);
    if (user==null)
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      Share share = shareRepo.findById(shareId).get();
      if (share.user.getId()==user.getId()) {
        shareRepo.deleteById(shareId);
        return ResponseEntity.ok(new MessageResponse("delete share successfully"));
      } else {
        return ResponseEntity.ok(new MessageResponse("User is not authorized to delete this post"));
      }
   
  }

  @GetMapping("/myPosts")
  public ResponseEntity<?> findyPosts() {
    User user = userFromToken(request);
    if (user==null)
    return ResponseEntity.ok("Post not found");
  // if( !user.getAccountIsPrivate()||user.friends.contains(user)){l
      List<EntityModel<Post>> users = userRepo.findPostsByUserId(user.getId()).stream()
          .map(e -> postmodelAss.toModelpostId(e))
          .collect(Collectors.toList());
         if (users.isEmpty()) {
        throw new NFException(User.class);
      }
      return ResponseEntity.ok(CollectionModel.of(users,
          linkTo(methodOn(PostController.class).findAllPost()).withRel("Go to all Post")));
        // }
          // return ResponseEntity.ok(new MessageResponse("this account is private"));

    // }
    // 
    // return ResponseEntity.ok(
    //     CollectionModel.of(l, linkTo(methodOn(Controller.class).getUserById(user.getId())).withRel("User Profile")));
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
  public ResponseEntity<?> findFriendPosts( @PathVariable Long friendId) {
           User user = userFromToken(request);
           if (user==null)
   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      Optional<User> optionalFriend = userRepo.findById(friendId);
      if (optionalFriend.isPresent()) {
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
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("friend not found.");
      }
    // } else {
    //   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access. sign in");
    // }
  }
  

  @GetMapping("/postUser/{postid}")
  public ResponseEntity<EntityModel<User>> postUser(@PathVariable Long postid) {//owner
    Post post = postRepo.findById(postid).get();

    return ResponseEntity.ok(usermodelAss.toModeluserprofile(post.user));

  }


  
@PutMapping("/{commentId}/comment/edit")
public ResponseEntity<?> editCoumment(@PathVariable Long commentId, @RequestBody String newContent) {
  User user = userFromToken(request);
  if (user==null)
return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
Comment comment =commentRepo.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
comment.setContent(newContent);
commentRepo.save(comment);

  return ResponseEntity.ok(commentmodelAss.commentDelEdit(comment));
}


@GetMapping("/{id}/user")
public ResponseEntity<?> getUserPost(@PathVariable Long id) {
  User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("user not found"));
  List<Post> userPost =user.posts;

  List<EntityModel<Post>> users = userPost.stream()
  .map(e -> postmodelAss.toModelpostId(e))
  .collect(Collectors.toList());
 if (users.isEmpty()) {
throw new NFException(User.class);
}
return ResponseEntity.ok(CollectionModel.of(users,
  linkTo(methodOn(PostController.class).findAllPost()).withRel("Go to all Post")));}








@GetMapping("/reels")
public ResponseEntity<?> getReals(Comment comment, HttpServletRequest request) {
  return null;
}
@GetMapping("/reels/{id}/friend")
public ResponseEntity<?> getRealsFriend(Comment comment, HttpServletRequest request) {
  return null;
}
@PutMapping("/{id}/editShare")
public ResponseEntity<?> editShare(Comment comment, HttpServletRequest request) {
  return null;
}

@PutMapping("/{id}/editPost")
public ResponseEntity<?> editPost(Comment comment, HttpServletRequest request) {
  return null;
}

@PutMapping("/share/{id}/createLike")
public ResponseEntity<?> createrShareLike(Comment comment, HttpServletRequest request) {
  return null;
}
@PutMapping("/share/{id}/createComment")
public ResponseEntity<?> createShareComment(Comment comment, HttpServletRequest request) {
  return null;
}
@PostMapping("/reals/create")
public ResponseEntity<?> createReal(Comment comment, HttpServletRequest request) {
  return null;
}
// @DeleteMapping
// public ResponseEntity<?> deleteRels(Comment comment, HttpServletRequest request) {
//   return null;
// }


















public User userFromToken(HttpServletRequest request){
  String jwt = parseJwt(request);
  if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
      String username = jwtUtils.getUserNameFromJwtToken(jwt);
      User user = userRepo.findByUsername(username)
              .orElseThrow(() -> new RuntimeException("User not found"));
            return user;}
  return null;
}
}