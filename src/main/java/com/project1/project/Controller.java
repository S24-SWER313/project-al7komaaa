package com.project1.project;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project1.project.Comment.Comment;
import com.project1.project.Comment.CommentRepo;
import com.project1.project.Like.Like;
import com.project1.project.Like.LikeRepo;
import com.project1.project.Post.Post;
import com.project1.project.Post.PostRepo;
import com.project1.project.Share.Share;
import com.project1.project.Share.ShareRepo;
import com.project1.project.User.User;
import com.project1.project.User.UserRepo;

import jakarta.transaction.Transactional;

@RestController
public class Controller {
    private final   CommentRepo commentRepo;
    private final   LikeRepo likeRepo;
    private final  PostRepo postRepo;
    private final  ShareRepo shareRepo;
    private final  UserRepo userRepo;
    public Controller(CommentRepo commentRepo, LikeRepo likeRepo, PostRepo postRepo, ShareRepo shareRepo,
            UserRepo userRepo) {
        this.commentRepo = commentRepo;
        this.likeRepo = likeRepo;
        this.postRepo = postRepo;
        this.shareRepo = shareRepo;
        this.userRepo = userRepo;
    }


    
//   @Transactional
//   @GetMapping("/Users")
//   CollectionModel<EntityModel<User>> allUser() {

//     List<EntityModel<Patient>> patients = repository.findAll().stream() //
//       .map(assembler::toModel) //
//       .collect(Collectors.toList());
//     return CollectionModel.of(patients, linkTo(methodOn(PatientController.class).all()).withSelfRel());
   // }


//    @Transactional
//   @GetMapping("/Users")
//     CollectionModel<EntityModel<User>> allUser() {
//         return null;


//     }
@GetMapping("/users")
public List<User> getAllUsers() {
    return userRepo.findAll();
}
@GetMapping("/user/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepo.findById(id)
                         .orElseThrow(() -> new Exception(userRepo.findById(id).getClass()));
    }
    
    @GetMapping("/userByFirstName/{name}") //netzakar eno ne3malha non-CaseSensitive
    public List<User> getUserByFirstName(@PathVariable String name) {

        return userRepo.findByFirstName(name);}
    //                      .orElseThrow(() -> new Exception(userRepo.findByFirstName(id).getClass()));
    // }

    @GetMapping("/userByLastName/{name}") //netzakar eno ne3malha non-CaseSensitive
    public List<User> getUserByLastName(@PathVariable String name) {

        return userRepo.findByLastName(name);}


        @GetMapping("/username/{name}") //netzakar eno ne3malha non-CaseSensitive
        public List<User> getUserName(@PathVariable String name) {
    
            // return userRepo.findByName(name).orElse(userRepo.findByFirstName(name));}
       
          
                Optional<User> userOptional = userRepo.findByName(name);
                
                if (userOptional.isPresent()) {
                    return userOptional.get();
                } else {
                    List<User> users = userRepo.findByFirstName(name);
                    if (!users.isEmpty()) {
                        // Assuming you want to return the first user in the list
                        return users.get(0);
                    } else {
                        // Handle the case when no user is found
                        return null; // or throw an exception, return a default user, etc.
                    }
                }
            }
            
       
       
       
       
        }
    



    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return userRepo.save(user);
    }

    


    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        return postRepo.findAll();
    }

    @PostMapping("/posts")
    public Post createPost(@RequestBody Post post) {
        return postRepo.save(post);
    }

    @GetMapping("/posts/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postRepo.findById(id)
                             .orElseThrow(() -> new Exception(postRepo.findById(id).getClass()));
    }

    @GetMapping("/comments")
    public List<Comment> getAllComments() {
        return commentRepo.findAll();
    }

    @PostMapping("/comments")
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        Comment savedComment = commentRepo.save(comment);
        return ResponseEntity.created(URI.create("/comments/" )).body(savedComment);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        return commentRepo.findById(id)
                                .map(comment -> ResponseEntity.ok().body(comment))
                                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/likes")
    public List<Like> getAllLikes() {
        return likeRepo.findAll();
    }

    @PostMapping("/likes")
    public ResponseEntity<Like> createLike(@RequestBody Like like) {
        Like savedLike = likeRepo.save(like);
        return ResponseEntity.created(URI.create("/likes/" )).body(savedLike);
    }

    @GetMapping("/likes/{id}")
    public ResponseEntity<Like> getLikeById(@PathVariable Long id) {
        return likeRepo.findById(id)
                             .map(like -> ResponseEntity.ok().body(like))
                             .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/shares")
    public List<Share> getAllShares() {
        return shareRepo.findAll();
    }

    @PostMapping("/shares")
    public ResponseEntity<Share> createShare(@RequestBody Share share) {
        Share savedShare = shareRepo.save(share);
        return ResponseEntity.created(URI.create("/shares/")).body(savedShare);
    }

    @GetMapping("/shares/{id}")
    public ResponseEntity<Share> getShareById(@PathVariable Long id) {
        return shareRepo.findById(id)
                              .map(share -> ResponseEntity.ok().body(share))
                              .orElse(ResponseEntity.notFound().build());
    }








}