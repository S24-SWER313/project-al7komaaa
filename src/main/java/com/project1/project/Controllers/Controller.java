package com.project1.project.Controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.project1.project.NFException;
import com.project1.project.Entity.Comment.Comment;
import com.project1.project.Entity.Comment.CommentRepo;
import com.project1.project.Entity.Like.Like;
import com.project1.project.Entity.Like.LikeRepo;
import com.project1.project.Entity.Post.Post;
import com.project1.project.Entity.Post.PostRepo;
import com.project1.project.Entity.Share.Share;
import com.project1.project.Entity.Share.ShareRepo;
import com.project1.project.Entity.User.User;
import com.project1.project.Entity.User.UserModelAss;
import com.project1.project.Entity.User.UserRepo;
import com.project1.project.Security.Jwt.JwtUtils;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

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
    public CollectionModel<EntityModel<User>> getAllUsers() {
        
  List<EntityModel<User>> employees = userRepo.findAll().stream() 
      .map(userModelAss::toModel) //
      .collect(Collectors.toList());

  return CollectionModel.of(employees, linkTo(methodOn(Controller.class).getAllUsers()).withSelfRel());
       
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new NFException(User.class));
        return ResponseEntity.ok(user);
    }
// اغير الريسبونس انتتي

  
    @GetMapping("/userByFirstName/{name}")
    public ResponseEntity<List<User>> getUserByFirstName(@PathVariable String name) {
        List<User> userList = userRepo.findByFirstname(name);
        if (userList.isEmpty()) {
            throw new NFException(User.class);
        }
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/userByLastName/{name}")// netzakar eno ne3malha non-CaseSensitive
    public ResponseEntity<List<User>> getUserByLastName(@PathVariable String name) {
        List<User> userList = userRepo.findByLastname(name);
        if (userList.isEmpty()) {
            throw new NFException(User.class);
        }
        return ResponseEntity.ok(userList);
    }

  @GetMapping("/fullName/{name}")
public ResponseEntity<List<User>> getFullName(@PathVariable String name) {
    List<User> userList = new ArrayList<>();
    userList.addAll(userRepo.findByFirstname(name));
    userList.addAll(userRepo.findByLastname(name));
    userList.addAll(userRepo.findByFullname(name));
userList.stream().distinct();
    if (userList.isEmpty()) {
        throw new NFException(User.class);
    }
    return ResponseEntity.ok(userList);
}

    
       
@GetMapping("/UserName/{name}")
public ResponseEntity<User> getUserName(@PathVariable String name) {
    Optional<User> user = userRepo.findByUsername(name);
    if (user.isPresent()) {
        return ResponseEntity.ok(user.get());
    } else {
        throw new EntityNotFoundException("User not found with username: " + name);
    }
}



@GetMapping("/UserFriend/{userid}")
public List<User> getUserFriend(@PathVariable Long userid){

    User user = userRepo.findById(userid).get();
return user.friends;

}




@PostMapping("/AddUserFriend/{userfriendid}")
public ResponseEntity<String> addFriend(HttpServletRequest request, @PathVariable Long userfriendid) {

    String jwt = parseJwt(request);
    if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepo.findByUsername(username)
                            .orElseThrow(() -> new RuntimeException("User not found"));
        User friend = userRepo.findById(userfriendid)
                              .orElseThrow(() -> new RuntimeException("Friend not found"));

        // Check if the friend already exists in the user's friends list
        boolean alreadyExists = user.friends.contains(friend);
        boolean alreadyExistss = friend.friends.contains(user);
        // If the friend doesn't already exist, add them to the user's friends list
        if (!alreadyExists && !alreadyExistss) {
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
                            .orElseThrow(() -> new RuntimeException("User not found"));
        User friend = userRepo.findById(userid)
                              .orElseThrow(() -> new RuntimeException("Friend not found"));

        boolean alreadyExists = user.friends.contains(friend);
        boolean alreadyExistss = friend.friends.contains(user);

        if (alreadyExists && alreadyExistss) {
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

//     User user = userRepo.findById(userid).get();
// return user.friends;









 private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7);
    }

    return null;
  }


}