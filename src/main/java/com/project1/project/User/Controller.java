package com.project1.project.User;

import java.net.URI;
import java.util.List;
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
import com.project1.project.Comment.Comment;
import com.project1.project.Comment.CommentRepo;
import com.project1.project.Like.Like;
import com.project1.project.Like.LikeRepo;
import com.project1.project.Post.Post;
import com.project1.project.Post.PostRepo;
import com.project1.project.Share.Share;
import com.project1.project.Share.ShareRepo;

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
    public User getUserById(@PathVariable Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new NFException(User.class));
    }

    @GetMapping("/userByFirstName/{name}") // netzakar eno ne3malha non-CaseSensitive
    public List<User> getUserByFirstName(@PathVariable String name) {

        List<User> nameList = userRepo.findByFirstName(name);
        if (nameList.size() == 0)
            new NFException(userRepo.findByFirstName(name).getClass());
        return nameList;
    }

    @GetMapping("/userByLastName/{name}") // netzakar eno ne3malha non-CaseSensitive
    public List<User> getUserByLastName(@PathVariable String name) {
        return userRepo.findByLastName(name);
    }
    @GetMapping("/userName/{name}")
    public List<User> getUserName(@PathVariable String name) {
        List<User> userName = userRepo.findByFullName(name);
        if (userName.isEmpty()) {
            List<User> usersByFirstName = userRepo.findByFirstName(name);
            if (usersByFirstName.isEmpty()) {
                return userRepo.findByLastName(name);
            } else {
                return usersByFirstName;
            }
        } else {
            return userName;
        }
    }

}