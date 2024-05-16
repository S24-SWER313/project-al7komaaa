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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
import jakarta.servlet.http.HttpServletResponse;
import jakarta.persistence.EntityNotFoundException;

import jakarta.transaction.Transactional;

import org.springframework.http.MediaType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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
  private NotificationController notificationController;
  
  private ImageUploadController imageUploadController=new ImageUploadController();

  @Autowired
  private LikeRepo likeRepo;

  List<EntityModel<Post>> l = new ArrayList<>();
  @GetMapping("/posts/random")
  public ResponseEntity<CollectionModel<EntityModel<Post>>> getRandomPosts() {
    User user = userFromToken(request);
   
List<EntityModel<Post>> users = postRepo.findRandom5Posts().stream().filter(e->e.getUser().getAccountIsPrivate()==false||user.friends.contains(e.getUser()))
    .map(postmodelAss::toModel)
    .collect(Collectors.toList());


return ResponseEntity
.ok(CollectionModel.of(users, linkTo(methodOn(PostController.class).getRandomPosts()).withRel("read more")));
}

@GetMapping("/{postId}/comments/random")
public ResponseEntity<?> getRandomComments(@PathVariable Long postId) {
  User user =userFromToken(request);
  Post post = postRepo.findById(postId).orElseThrow(() -> new NFException("post with ID " + postId + " not found."));
// if (post.getUser().friends.contains(user)||!post.getUser().getAccountIsPrivate()){
  //List<Comment> comments = post.postComments;
List<Comment>comments=commentRepo.findRandom5CommentsByPostId(postId);


  List<EntityModel<Comment>> commentModels = comments.stream()
      .map(com -> commentmodelAss.commentDelEdit(com))
      .collect(Collectors.toList());

  if (comments.isEmpty()) {
    MessageResponse noCommentsMessage = new MessageResponse("There are no comments for this post.");
   List<Comment> f= new ArrayList<Comment>();
    return ResponseEntity.ok(CollectionModel.of(f,
    linkTo(methodOn(PostController.class).getRandomComments(postId)).withRel("Read more")));

  }
  return ResponseEntity.ok(CollectionModel.of(commentModels,
      linkTo(methodOn(PostController.class).getRandomComments(postId)).withRel("Read more")));

}

  @PostMapping("/create")
  public ResponseEntity<?> createPost( @RequestBody Post post) {
     User user = userFromToken(request);
        if (user==null||post==null)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Post or user not found"));

      post.setUser(user);
      postRepo.save(post);
      return ResponseEntity.ok(new MessageResponse("Post Created successfully!"));
  }
  @PostMapping("{id}/create/image")
  public ResponseEntity<?> addImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
    Post post = postRepo.findById(id).orElseThrow(() -> new NFException("post with ID " + id + " not found."));
 String im=   imageUploadController.uploadImage(file);
    post.setImage(im);
      postRepo.save(post);
      return ResponseEntity.ok(new MessageResponse(im));
  }
  @GetMapping("/{postId}/comment")
  public ResponseEntity<?> getAllPostComments(@PathVariable Long postId) {
User user =userFromToken(request);
    Post post = postRepo.findById(postId).orElseThrow(() -> new NFException("post with ID " + postId + " not found."));
if (post.getUser().friends.contains(user)||!post.getUser().getAccountIsPrivate()){
    //List<Comment> comments = post.postComments;
List<Comment>comments=postRepo.postComments(postId);
if (comments.isEmpty()) {
  return ResponseEntity.ok(new MessageResponse("no comment in this post"));
}
    List<EntityModel<Comment>> commentModels = comments.stream()
        .map(com -> commentmodelAss.commentDelEdit(com))
        .collect(Collectors.toList());

    if (comments.isEmpty()) {
      MessageResponse noCommentsMessage = new MessageResponse("There are no comments for this post.");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noCommentsMessage);
    }
    return ResponseEntity.ok(CollectionModel.of(commentModels,
        linkTo(methodOn(PostController.class).findById(postId)).withRel("Go to Post")));}
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("this user's profile is private add him to see comment of this post ");

  }

  @GetMapping("/posts")
  public ResponseEntity<?> findAllPost() {

        User user = userFromToken(request);
        if (user==null)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("user not found"));

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
                                          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("user not found"));
                                          Post post = postRepo.findById(postId).orElse(null);
          if (post == null) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
          }
          if(!post.getUser().getAccountIsPrivate()||post.getUser().equals(user)){
          Share share = new Share(content.trim(), user, post);
          shareRepo.save(share);
         // EntityModel<Share> entityModel = EntityModel.of();
         notificationController.send("this user "+user.getUsername()+" shared your post", post.getUser().getId());
          return ResponseEntity.ok(postmodelAss.toModelsharepostId(share));}else{
            return ResponseEntity.badRequest().body("this post is private");
          }
  
  }


  @GetMapping("/share/{shareId}")
  public ResponseEntity<?> findshareById(@PathVariable Long shareId)  {
Share share = shareRepo.findById(shareId).orElseThrow(() -> new NFException("share with ID " + shareId + " not found."));               ;

    User user = userFromToken(request);
        if (user==null)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("user not found"));
if(!share.getUser().getAccountIsPrivate()||user.friends.contains(share.getUser())||share.getUser().equals(user))
return ResponseEntity.ok(postmodelAss.toModelsharepostId(share)); 
return ResponseEntity.badRequest().body("this post is private");
  }
  
  @GetMapping("/posts/{postId}")
  public ResponseEntity<?> findById(@PathVariable Long postId)  {
Post post = postRepo.findById(postId).orElseThrow(() -> new NFException("post with ID " + postId + " not found."));

    User user = userFromToken(request);
        if (user==null)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("user not found"));
if(!post.getUser().getAccountIsPrivate()||user.friends.contains(post.getUser())||post.getUser().equals(user))
return ResponseEntity.ok(postmodelAss.toModelpostId(post)); 
return ResponseEntity.badRequest().body("this post is private");
  }

  //
  @DeleteMapping("/{postId}")
    public ResponseEntity<?> deleteById(@PathVariable Long postId) {
      User user = userFromToken(request);
          if (user==null)
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("user not found"));
       Post post= postRepo.findById(postId).orElseThrow(() -> new NFException("post with ID " + postId + " not found."));
                      
          if (userHasPermissionToDeletePost(postId,user.getId())) {
          postRepo.deleteById(postId);
    return ResponseEntity.ok().body("the post is deleted successfully");
       }else {
           return  ResponseEntity.badRequest().body("you are not owned the post");
        }}
  

        @GetMapping("/number/comment/{postId}")
        public ResponseEntity<?> numberComment(@PathVariable Long postId) {
          if (commentRepo.countCommentsByPostId(postId)==0||commentRepo.countCommentsByPostId(postId)==null)
          return ResponseEntity.ok((long) 0);
            return ResponseEntity.ok(commentRepo.countCommentsByPostId(postId)) ;
        }
  /////////////////////////////////////////////////////////////////////
  @PostMapping("/comment/{postId}/post")
  public ResponseEntity<?> createComment( @RequestBody Comment comment, @PathVariable Long postId) {
        User user = userFromToken(request);
        if (user==null)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("user not found"));

      Post post = postRepo.findById(postId).orElseThrow(() -> new NFException("post with ID " + postId + " not found."));
      if (post.getUser().friends.contains(user)||!post.getUser().getAccountIsPrivate()){
      post.postComments.add(comment);
      comment.setUser(user);
      comment.setPost(post);
      // user.comments.add(comment);
      userRepo.save(user);
      postRepo.save(post);
      commentRepo.save(comment);
      notificationController.send("this user "+user.getUsername()+" commented on your post", post.getUser().getId());

      return ResponseEntity.ok(commentmodelAss.commentDelEdit(comment));}
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("this user's profile is private add him to write a comment of this post ");
}
@PostMapping("comment/{id}/image")
public ResponseEntity<?> addImageComment(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
  Comment comment = commentRepo.findById(id).orElseThrow(() -> new NFException("comment with ID " + id + " not found."));
String im=   imageUploadController.uploadImage(file);
  comment.setImage(im);
  commentRepo.save(comment);
    return ResponseEntity.ok(new MessageResponse("added image successfully"));
}

  @DeleteMapping("/comment/{commentId}")
  public ResponseEntity deleteComment(@PathVariable Long commentId) {
    User user = userFromToken(request);
        if (user==null)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("user not found"));
      // List<Comment>postComments=postRepo.postComments(postId);
      Comment comment = commentRepo.findById(commentId).orElseThrow(() -> new NFException("comment with ID " + commentId + " not found."));
      User userComment = comment.getUser();

      if (userHasPermissionToDeleteComment(commentId, user.getId())) {
        commentRepo.deleteById(commentId);
      } else {
        return ResponseEntity.ok(new MessageResponse("you are not authorized!"));
      }
      return ResponseEntity.ok(new MessageResponse("Delete successfully!"));
  
  }

  private boolean userHasPermissionToDeleteComment(Long commentId, Long userId) {

    Comment comment = commentRepo.findById(commentId).orElseThrow(() -> new NFException("comment with ID " + commentId + " not found."));

    return comment.getUser().getId().equals(userId);
  }

  ////////////////////////////////////////////////////////////////////
  @GetMapping("/user/like")
  public ResponseEntity<?> findByLikesContainsUser() {
                                                                                                             
    User user = userFromToken(request);
    if (user==null)
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("user not found"));
    List<Like> likeList = likeRepo.findByUser(user);
      List<Post> likePost = likeList.stream().map(e -> e.post).filter(e->e!=null).collect(Collectors.toList());
      ///////////////////
      List<EntityModel<Post>> users = likePost.stream()
          .map(e -> postmodelAss.toModel(e))
          .collect(Collectors.toList());

      if (users.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("the user hasn't made any likes yet"));
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
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("user not found"));
    Post post = postRepo.findById(postId).orElseThrow(() -> new NFException("post with ID " + postId + " not found."));
   
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
      notificationController.send("this user "+user.getUsername()+" liked your post", post.getUser().getId());

      return ResponseEntity.status(HttpStatus.CREATED).body(savedLike);
   
  }

  
  // @GetMapping("/{postId}/isLike")
  // public List<Like> hasLiked( Long postId) {
  //   User user=userFromToken(request);
    
  //         List<Like> userLikes = postRepo.findByUser(postId);
  //         List<Like> c=userLikes.stream().filter((like)->
  //      like.getUser().equals( user )).collect(Collectors.toList());
  //         return userLikes ;
  // }

  @GetMapping("/{postId}/likes")
  public ResponseEntity<List<EntityModel<Like>>> getAllPostLikes(@PathVariable Long postId) {
   Post post= postRepo.findById(postId).orElseThrow(() -> new NFException("post with ID " + postId + " not found."));

   
    // List<Like> likes = postRepo.findLikes(postId);
    List<Like> likes = likeRepo.findByPost(post);

    List<EntityModel<Like>> entityModels = likes.stream()
        .map(us -> postmodelAss.toModelPostLike(us))
        .collect(Collectors.toList());

    return ResponseEntity.ok(entityModels);
  }
  @GetMapping("/{postId}/like/random")
  public ResponseEntity<List<EntityModel<Like>>> getRandomLikes(@PathVariable Long postId) {
 
    List<Like> likes = likeRepo.findRandom5LikesByLikeId(postId);
    List<EntityModel<Like>> entityModels = likes.stream()
    .map(us -> postmodelAss.toModelPostLike(us))
    .collect(Collectors.toList());
    return ResponseEntity.ok(entityModels);
}
@GetMapping("/{postId}/isLikes")
public   ResponseEntity<?> checkIfUserLikedPost( @PathVariable Long postId) {
  User user=userFromToken(request);
  Post post = postRepo.findById(postId).orElseThrow(() -> new NFException("post with ID " + postId + " not found."));
  List<Like> likes = likeRepo.findByUserAndPost(user, post);
  return  ResponseEntity.ok(likes) ;

}
@GetMapping("/number/like/{postId}")
public ResponseEntity<?> numberLike(@PathVariable Long postId) {
  if (likeRepo.countLikesByLikeId(postId)==0||likeRepo.countLikesByLikeId(postId)==null)
  return ResponseEntity.ok((long) 0);
    return ResponseEntity.ok(likeRepo.countLikesByLikeId(postId)) ;
}


@GetMapping("/number/post/{userId}")
public Long numberpost(@PathVariable Long userId) {
    return postRepo.countPostsByUserIdNative(userId);
}



  // @GetMapping("/{postId}/shares")
  //   public List<Share> getSharesByPost(@PathVariable Long postId) {
  //       Post post = postRepo.findById(postId).orElseThrow(() -> new NFException("like with ID " + postId + " not found."));
  //      if (post.shares.isEmpty())
  //      return Collections.emptyList();
  //                         else
  //                  return post.shares;
      
  //   }
  @DeleteMapping("/{likeId}/like")
  public ResponseEntity<MessageResponse> UnCreatelikePost(@PathVariable Long likeId) {
    Like like=likeRepo.findById(likeId).orElseThrow(() -> new NFException("like with ID " + likeId + " not found."));
    User user=userFromToken(request);
    if (like.user==user){
    likeRepo.deleteById(likeId);
    return ResponseEntity.ok(new MessageResponse("Delete successfully!"));}
    return ResponseEntity.ok(new MessageResponse("you are not the owner of like"));
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
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("user not found"));

      Share share = shareRepo.findById(shareId).orElseThrow(() -> new NFException("share with ID " + shareId + " not found."));
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
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("user not found"));

  // if( !user.getAccountIsPrivate()||user.friends.contains(user)){l
      List<EntityModel<Post>> users = userRepo.findPostsByUserId(user.getId()).stream()
          .map(e -> postmodelAss.toModelpostId(e))
          .collect(Collectors.toList());
         if (users.isEmpty()) {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("there are no posts for this user"));
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

    Post post = postRepo.findById(postId).orElseThrow(() -> new NFException("post with ID " + postId + " not found."));

    return post != null && post.user.getId().equals(userId);
  }

  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7);
    }

    return null;
  }

  @GetMapping("/friendPosts/{friendId}")
  public ResponseEntity<?> findFriendPosts( @PathVariable Long friendId) {
           User user = userFromToken(request);
           if (user==null)
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("user not found"));
        User friend = userRepo.findById(friendId).orElseThrow(() -> new NFException("friend with ID " + friendId + " not found."));
     
        boolean isFriend = user.friends.contains(friend);

        if (isFriend||friendId==user.getId()) {
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
    
    // } else {
    //   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access. sign in");
    // }
  }
  









  

  @GetMapping("/postUser/{postid}")
  public ResponseEntity<EntityModel<User>> postUser(@PathVariable Long postid) {//owner ,user who owner the post
    Post post = postRepo.findById(postid).orElseThrow(() -> new NFException("post with ID " + postid + " not found."));

    return ResponseEntity.ok(usermodelAss.toModeluserprofile(post.user));

  }


  
@PutMapping("/{commentId}/comment/edit")
public ResponseEntity<?> editCoumment(@PathVariable Long commentId, @RequestBody String newContent) {
  User user = userFromToken(request);
  if (user==null)
  return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("user not found"));

Comment comment =commentRepo.findById(commentId).orElseThrow(() -> new NFException("comment with ID " + commentId + " not found."));
if(user==comment.getUser()){
comment.setContent(newContent);
commentRepo.save(comment);

  return ResponseEntity.ok(commentmodelAss.commentDelEdit(comment));
}
return ResponseEntity.ok("you aren't the owner of this comment ");
}


@GetMapping("/{id}/user")
public ResponseEntity<?> getUserPost(@PathVariable Long id) {
  User user = userRepo.findById(id).orElseThrow(() -> new NFException("user with ID " + id + " not found."));
  User signInUser = userFromToken(request);
  if(user.getId()!=id){
 if(user.getAccountIsPrivate() && !user.friends.contains(signInUser)){
  return ResponseEntity.ok("the account is private you cant view");
 }
}
 else{
  List<Post> userPost =user.posts;

  List<EntityModel<Post>> users = userPost.stream()
  .map(e -> postmodelAss.toModel(e))
  .collect(Collectors.toList());
 if (users.isEmpty()) {
  return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("there are no posts for this user"));
}
return ResponseEntity.ok(CollectionModel.of(users,
  linkTo(methodOn(PostController.class).findAllPost()).withRel("Go to all Post")));}
  return null;


}


@GetMapping("/postImage/{postId}")
public ResponseEntity<byte[]> getPostImge(@PathVariable Long postId) {
    Post post = postRepo.findById(postId).orElse(null);
    if (post == null || post.getImage() == null || post.getImage().isEmpty()) {
        return ResponseEntity.notFound().build();
    }
    try {
        Path imagePath = Paths.get( post.getImage());
        byte[] imageBytes = Files.readAllBytes(imagePath);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    } catch (IOException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

@GetMapping("/reels")
public ResponseEntity<?> getReals() {
  
  User user = userFromToken(request);
  if (user==null)
  return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("user not found"));
  List <Post> posts = postRepo.findAll().stream().filter(e->(e.getUser().getAccountIsPrivate()==false||user.friends.contains(e.getUser())||e.getUser()==user))
  // .map(postmodelAss::toModel)
  .collect(Collectors.toList());
  List<EntityModel<Post>> reels = posts.stream().filter(e->e.getVideo() != null).map(postmodelAss::toModel).collect(Collectors.toList());

if (reels.isEmpty()) {
MessageResponse errorMessage = new MessageResponse("No reels found.");
return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
}
return ResponseEntity
  .ok(CollectionModel.of(reels, linkTo(methodOn(PostController.class).findAllPost()).withRel("Go to all Posts")));

 
}









@GetMapping("/getVideo/{postId}")
public ResponseEntity<byte[]> getVideo(@PathVariable Long postId) {
    Post post = postRepo.findById(postId).orElseThrow(() -> new NFException("video not found."));
   
    
    String videoPath = post.getVideo(); // Assuming user has a 'video' field containing the path to the video
    
    if (videoPath == null || videoPath.isEmpty()) {
        return ResponseEntity.notFound().build();
    }
    
    try {
        Path videoFilePath = Paths.get(videoPath);
        byte[] videoBytes = Files.readAllBytes(videoFilePath);
        return ResponseEntity.ok().contentType(MediaType.valueOf("video/mp4")).body(videoBytes);
    } catch (IOException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}










// @GetMapping("/reels/{id}/friend")
// public ResponseEntity<?> getRealsFriend(Comment comment, HttpServletRequest request) {
//   return null;
// }



@PutMapping("/{shareId}/editShare")
public ResponseEntity<?> editShare(@PathVariable Long shareId, @RequestBody String editShare ) {
  User user = userFromToken(request);
  if (user==null)
  return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("user not found"));

  Share share = shareRepo.findById(shareId).orElseThrow(() -> new NFException("share with ID " + shareId + " not found."));
  if(share.user==user){       
  share.setContent(editShare);
  shareRepo.save(share);

  return ResponseEntity.ok(postmodelAss.toModelsharepostId(share));
}
return ResponseEntity.ok("you aren't the owner of this post ");
}




@PutMapping("/{id}/editPost")
public ResponseEntity<?> editPost(@RequestBody Post newpost ,@PathVariable Long id) {
  User user = userFromToken(request);
  if (user==null)
  return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("user not found"));

  Post oldpost = postRepo.findById(id).orElseThrow(() -> new NFException("Post with ID " + id + " not found."));

  if(oldpost.user==user){       
  oldpost.setContent(newpost.getContent());
// oldpost.setImage(newpost.getImage());
// oldpost.setVideo(newpost.getVideo());
  postRepo.save(oldpost);

  return ResponseEntity.ok(postmodelAss.toModelpostId(oldpost));
}
return ResponseEntity.ok("you aren't the owner of this post ");
}


@PostMapping("/share/{shareId}/createLike")
public ResponseEntity<?> createrShareLike(@RequestBody Like like, @PathVariable Long shareId) {
  User user = userFromToken(request);
    if (user==null)
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("user not found"));
    Share share = shareRepo.findById(shareId).orElseThrow(() -> new NFException("share with ID " + shareId + " not found."));
    

      if (share.like.stream().anyMatch(l -> l.user.getId().equals(user.getId()))) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User has already liked the postShare");
      }
else{
   
 
      like.setUser(user);
      like.setShare(share);
       user.likes.add(like);
       share.like.add(like);  }
   
       likeRepo.save(like);
      
       notificationController.send("this user "+user.getUsername()+" liked your share ", share.getUser().getId());

      return ResponseEntity.ok("created like successfully");
}






@PostMapping("/share/{id}/createComment")
public ResponseEntity<?> createShareComment( @RequestBody Comment comment, @PathVariable Long id) {
  User user = userFromToken(request);
        if (user==null)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("user not found"));
      Share share = shareRepo.findById(id).orElseThrow(() -> new NFException("share with ID " + id + " not found."));
    
      share.sharComments.add(comment);
      
      comment.setUser(user);
      comment.setShare(share);
      userRepo.save(user);
      shareRepo.save(share);
      commentRepo.save(comment);
      notificationController.send("this user "+user.getUsername()+" commented on your share ", share.getUser().getId());

      return ResponseEntity.ok(commentmodelAss.commentDelEdit(comment));
}


@PostMapping("/reals/create")
public ResponseEntity<?> createReal( @RequestParam("file") MultipartFile video) {
  User user = userFromToken(request);
  if (user==null)
  return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("user not found"));
//   if (video.getSize() > 10000 * 1024 * 1024) { // 10000MB in bytes
//     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File size should not exceed 10000MB");
// }
   Post post=new Post();
   String im = imageUploadController.uploadImage(video);
   post.setVideo(im);
if(post.getVideo()!= null){
post.setUser(user);
postRepo.save(post);
return ResponseEntity.ok(new MessageResponse("Reel Created successfully!"));
}
return ResponseEntity.ok(new MessageResponse("must be video and content"));
}

//worked as edit content 
@PostMapping("/{id}/reals/create/content")
public ResponseEntity<?> createRealcontent(@RequestBody String content ,@PathVariable Long id) {
  Post post = postRepo.findById(id).orElseThrow(() -> new NFException("Post with ID " + id + " not found."));
  User user = userFromToken(request);
  if (user==post.user){
post.setContent(content);
   postRepo.save(post);
return ResponseEntity.ok(new MessageResponse("Reel content Created successfully!"));}
return ResponseEntity.ok("you aren't the owner of this real ");

}







public User userFromToken(HttpServletRequest request){
  String jwt = parseJwt(request);
  if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
      String username = jwtUtils.getUserNameFromJwtToken(jwt);
      User user = userRepo.findByUsername(username).orElseThrow(() -> new NFException("user not found."));
            return user;}
  return null;
}
}