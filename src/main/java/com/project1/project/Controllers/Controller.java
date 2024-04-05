package com.project1.project.Controllers;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.project1.project.NFException;
import com.project1.project.Entity.Comment.Comment;
import com.project1.project.Controllers.ImageUploadController;

import com.project1.project.Entity.Comment.CommentRepo;
import com.project1.project.Entity.Like.Like;
import com.project1.project.Entity.Like.LikeRepo;
import com.project1.project.Entity.Post.Post;
import com.project1.project.Entity.Post.PostRepo;
import com.project1.project.Entity.Share.Share;
import com.project1.project.Entity.Share.ShareRepo;
import com.project1.project.Entity.User.Gender;
import com.project1.project.Entity.User.User;
import com.project1.project.Entity.User.UserModelAss;
import com.project1.project.Entity.User.UserRepo;
import com.project1.project.Payload.Response.MessageResponse;
import com.project1.project.Security.Jwt.JwtUtils;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
public class Controller {
    private final CommentRepo commentRepo;
    private final LikeRepo likeRepo;
    private final PostRepo postRepo;
    private final ShareRepo shareRepo;
    private final UserRepo userRepo;
      @Autowired
  private JwtUtils jwtUtils;
    @Autowired
private UserModelAss userModelAss;
@Autowired
HttpServletRequest request;

ImageUploadController imageUploadController=new ImageUploadController();
    public Controller(CommentRepo commentRepo, LikeRepo likeRepo, PostRepo postRepo, ShareRepo shareRepo,
            UserRepo userRepo) {
        this.commentRepo = commentRepo;
        this.likeRepo = likeRepo;
        this.postRepo = postRepo;
        this.shareRepo = shareRepo;
        this.userRepo = userRepo;
       
    }
    @Transactional
    @GetMapping("/users")
    public ResponseEntity<CollectionModel<EntityModel<User>>> getAllUsers(HttpServletRequest request) {
        List<EntityModel<User>> users = userRepo.findAll().stream()
                .map(user -> userModelAss.toModelfriendself(user))
                .collect(Collectors.toList());
    
                return ResponseEntity.ok(CollectionModel.of(users, linkTo(methodOn(Controller.class).getAllUsers(request)).withSelfRel()));
    }
    

    @GetMapping("/user/{id}")
public ResponseEntity<?> getUserById(@PathVariable Long id) {

    User user = userRepo.findById(id)
    .orElseThrow(() -> new NFException("user with ID " + id + " not found."));
    EntityModel<User> entityModel = userModelAss.toModeluserprofile(user);
    // if (user.getAccountIsPrivate()||user!=me||!me.friends.contains(user))
    // return ResponseEntity.ok("this account is private to see user post addFriend");
    return ResponseEntity.ok(entityModel);
}

// اغير الريسبونس انتتي

  
    @GetMapping("/userByFirstName/{name}")
    public ResponseEntity<?> getUserByFirstName(@PathVariable String name) {
        List<User> userList = userRepo.findByFirstname(name);
      //  EntityModel<User> entityModel = userModelAss.toModeluserprofile(name, request);
      List<EntityModel<User>> users = userList.stream()
      .map(user -> userModelAss.toModelfriendself(user))
      .collect(Collectors.toList());
        if (users.isEmpty()) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("there are no user with firstname "+ name));
        }
        
        return ResponseEntity.ok(CollectionModel.of(users, linkTo(methodOn(PostController.class).findAllPost()).withRel("Go to all Posts")));
      
    }

    @GetMapping("/userByLastName/{name}")// netzakar eno ne3malha non-CaseSensitive
    public ResponseEntity<?> getUserByLastName(@PathVariable String name) {
        List<User> userList = userRepo.findByLastname(name);
        List<EntityModel<User>> users = userList.stream()
      .map(user -> userModelAss.toModelfriendself(user))
      .collect(Collectors.toList());
        if (users.isEmpty()) {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("there are no user with Lastname "+ name));
        }
        return ResponseEntity.ok(CollectionModel.of(users, linkTo(methodOn(PostController.class).findAllPost()).withRel("Go to all Posts")));
      
    }

  @GetMapping("/fullName/{name}")
public ResponseEntity<?>getFullName(@PathVariable String name) {
    List<User> userList = new ArrayList<>();
    userList.addAll(userRepo.findByFirstname(name));
    userList.addAll(userRepo.findByLastname(name));
    userList.addAll(userRepo.findByFullname(name));
    List<EntityModel<User>> users = userList.stream()
    .map(user -> userModelAss.toModelfriendself(user))
    .collect(Collectors.toList());
      if (users.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("there are no user with fullname "+ name));
      }
      return ResponseEntity.ok(CollectionModel.of(users, linkTo(methodOn(PostController.class).findAllPost()).withRel("Go to all Posts")));
    
}
       
@GetMapping("/UserName/{name}")
public ResponseEntity<?> getUserName(@PathVariable String name ) {
    User user = userRepo.findByUsername(name) .orElseThrow(() -> new NFException("user not found."));;
   
        EntityModel<User> entityModel = userModelAss.toModeluserprofile(user);
        // if (user.get().getAccountIsPrivate())
        // return ResponseEntity.ok("this account is private to see user post addFriend");
       return ResponseEntity.ok(entityModel);
    
    
}



@GetMapping("/userFriend/{userid}")
public ResponseEntity<CollectionModel<EntityModel<User>>> getUserFriend(@PathVariable Long userid ){

    User user = userRepo.findById(userid) .orElseThrow(() -> new NFException("user not found."));
    List<EntityModel<User>> users =userRepo.getFriends(userid).stream()
    .map(us -> userModelAss.toModelfriendself(us))
    .collect(Collectors.toList());
// return userRepo.getFriends(userid);
return ResponseEntity.ok(CollectionModel.of(users, linkTo(methodOn(Controller.class).getUserById(userid)).withRel("Go to all Posts")));

}




@PostMapping("/addUserFriend/{userfriendid}")
public ResponseEntity<String> addFriend(HttpServletRequest request, @PathVariable Long userfriendid) {

    String jwt = parseJwt(request);
    if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepo.findByUsername(username)
        .orElseThrow(() -> new NFException("user not found."));
        User friend = userRepo.findById(userfriendid)
        .orElseThrow(() -> new NFException("Friend not found."));

        // Check if the friend already exists in the user's friends list
        boolean alreadyExists = user.friends.contains(friend);
        boolean alreadyExistss = friend.friends.contains(user);
        // If the friend doesn't already exist, add them to the user's friends list
        if (!alreadyExists && !alreadyExistss ) {
            // friend.setFriend(true);
            // user.setFriend(true);
            user.friends.add(friend);
            friend.friends.add(user);
            userRepo.save(friend);
            userRepo.save(user);
            return ResponseEntity.ok("Friend added successfully");
        } else {
            return ResponseEntity.badRequest().body("Friend already exists");
        }
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("failed process");
}



@DeleteMapping("/deleteUserFriend/{userid}")
public ResponseEntity<String> deleteUserFriend(@PathVariable Long userid, HttpServletRequest request) {

    String jwt = parseJwt(request);
    if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepo.findByUsername(username)
        .orElseThrow(() -> new NFException("user not found."));
        User friend = userRepo.findById(userid)
        .orElseThrow(() -> new NFException("Friend not found."));

        boolean alreadyExists = user.friends.contains(friend);
        boolean alreadyExistss = friend.friends.contains(user);

        if (alreadyExists && alreadyExistss) {
            // friend.setFriend(false);
            // user.setFriend(false);
            user.friends.remove(friend);
            friend.friends.remove(user);
            userRepo.save(friend);
            userRepo.save(user);
            return ResponseEntity.ok("Friend removed successfully");
        } else {
            return ResponseEntity.badRequest().body("Friend not found in user's friend list");
        }
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
}


@PutMapping("/privacy")
public ResponseEntity<?> setPrivacy(@RequestBody boolean isprivate) {
    User user = userFromToken(request);
        if (user==null)
return ResponseEntity.ok("make sure you signed up");
user.setAccountIsPrivate(isprivate);
userRepo.save(user);
  return ResponseEntity.ok("the privacy of account is "+ isprivate);
}





 private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7);
    }

    return null;
  }
  public User userFromToken(HttpServletRequest request) {
    String jwt = parseJwt(request);
    if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        return userRepo.findByUsername(username)
        .orElseThrow(() -> new NFException("user not found."));
    }
    return null;
}


  
@PutMapping("/editFirstName")
public ResponseEntity<?> editFirstName(@RequestBody String newfirstName) {
  User user = userFromToken(request);
  if (user==null)
return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
user.setFirstname(newfirstName);
userRepo.save(user);
  return ResponseEntity.ok("firstName changed to" +user.getFirstname());
}
@PutMapping("/editLastName")
public ResponseEntity<?> editLastName(@RequestBody String newlastName) {
  User user = userFromToken(request);
  if (user==null)
return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
user.setLastname(newlastName);
userRepo.save(user);
  return ResponseEntity.ok("lastName changed to" +user.getLastname());
}

@PutMapping("/editMobile")
public ResponseEntity<?> editMobile(@RequestBody String mobile) {
  User user = userFromToken(request);
  if (user==null)
return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
user.setMobile(mobile);
userRepo.save(user);
  return ResponseEntity.ok("Mobile changed to" +user.getMobile());
}

@PutMapping("/editEmail")
public ResponseEntity<?> editEmail(@RequestBody String email) {
  User user = userFromToken(request);
  if (user==null)
return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
user.setEmail(email);
userRepo.save(user);
  return ResponseEntity.ok("Email changed to" +user.getEmail());
}


// @PutMapping("/editUsername")
// public ResponseEntity<?> editUsername(@RequestBody String username) {
//   User user = userFromToken(request);
//   if (user==null)
// return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
// user.setUsername(username);
// userRepo.save(user);
//   return ResponseEntity.ok("Username changed to" +user.getUsername());
// }


@PutMapping("/editGender")
public ResponseEntity<?> editGender(@RequestBody Gender gender) { //
  User user = userFromToken(request);
  if (user==null)
return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
user.setGender(gender);
userRepo.save(user);
  return ResponseEntity.ok("Gender changed to" +user.getGender());
}


@PutMapping("/editBio")
public ResponseEntity<?> editBio(@RequestBody String bio) {
  User user = userFromToken(request);
  if (user==null)
return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
user.setBio(bio);
userRepo.save(user);
  return ResponseEntity.ok("Bio changed to" +user.getBio());
}



@PutMapping("/editLocation")
public ResponseEntity<?> editLocation(@RequestBody String location) {
  User user = userFromToken(request);
  if (user==null)
return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
user.setLocation(location);
userRepo.save(user);
  return ResponseEntity.ok("Location changed to" +user.getLocation());
}


@PutMapping("/editImage")
public ResponseEntity<?> editImage(@RequestParam("file")MultipartFile file) {
  User user = userFromToken(request);
  if (user==null)
return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
String res=imageUploadController.uploadImage(file);
user.setImage(res);
userRepo.save(user);
  return ResponseEntity.ok(user.getImage());
}


@PutMapping("/editDof")
public ResponseEntity<?> editDof(@RequestBody LocalDate dateofbirth) {//
  User user = userFromToken(request);
  if (user==null)
return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
user.setDateofbirth(dateofbirth);
userRepo.save(user);
  return ResponseEntity.ok("Date of birth changed to" +user.getDateofbirth());
}


@PutMapping("/editBackgroundImage")
public ResponseEntity<?> editBackgroundImage(@RequestParam("file")MultipartFile file) {
  User user = userFromToken(request);
  if (user==null)
return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
String res=imageUploadController.uploadImage(file);
user.setBackgroudimage(res);
userRepo.save(user);
  return ResponseEntity.ok(user.getBackgroudimage());
}















}







