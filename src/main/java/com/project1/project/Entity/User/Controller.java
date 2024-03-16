package com.project1.project.Entity.User;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
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

import jakarta.transaction.Transactional;

@RestController
public class Controller {
    private final CommentRepo commentRepo;
    private final LikeRepo likeRepo;
    private final PostRepo postRepo;
    private final ShareRepo shareRepo;
    private final UserRepo userRepo;
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
public ResponseEntity<Optional<User>> getUserName(@PathVariable String name) {
    Optional<User> user = userRepo.findByUsername(name);
    if (!user.isPresent()) {
        throw new NFException(User.class);
    }
    return ResponseEntity.ok(user);
}

}