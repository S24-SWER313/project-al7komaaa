package com.project1.project.Entity.Post;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.LinkBuilder;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.project1.project.Controllers.PostController;
import com.project1.project.Entity.Comment.Comment;
import com.project1.project.Entity.Like.Like;
import com.project1.project.Entity.Share.Share;
import com.project1.project.Entity.User.User;
import com.project1.project.Entity.User.UserRepo;
import com.project1.project.Security.Jwt.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;
@Component
public class PostModelAss implements RepresentationModelAssembler<Post, EntityModel<Post>> {
  @Autowired
     HttpServletRequest request;
    @Autowired
    PostRepo postRepo;
    @Autowired
  UserRepo userRepo;
    @Autowired
    JwtUtils jwtUtils;
    @Override
    public EntityModel<Post> toModel(Post post) {
       
       return EntityModel.of(post,
                linkTo(methodOn(PostController.class).postUser(post.getPostId())).withRel("the post owner"),
               linkTo(methodOn(PostController.class).getAllPostLikes(post.getPostId())).withRel("the post's like"),
                linkTo(methodOn(PostController.class).createComment(null ,post.getPostId())).withRel("createComment"),
              linkTo(methodOn(PostController.class).createLikePost(null ,post.getPostId())).withRel("create like"),
              linkTo(methodOn(PostController.class).findByLikesContainsUser()).withRel("is liked"),
              // linkTo(methodOn(PostController.class).UnCreatelikePost(like.getLikeId())).withRel("delete your like"));

               linkTo(methodOn(PostController.class).getAllPostComments(post.getPostId())).withRel("the post's comment"));


    }

   
        public EntityModel<Post> toModelpostId(Post post)  {
             User user=null;
          String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
             user=userRepo.findByUsername(username).get();
           }
           if (userHasPermissionToDeletePost(post.getPostId(),user.getId())){
           return EntityModel.of(post,
           linkTo(methodOn(PostController.class).postUser(post.getPostId())).withRel("the post owner"),
          linkTo(methodOn(PostController.class).getAllPostLikes(post.getPostId())).withRel("the post's like"),
          linkTo(methodOn(PostController.class).getAllPostComments(post.getPostId())).withRel("the post's comment"),
          linkTo(methodOn(PostController.class).deleteById(post.getPostId())).withRel("delete your post"));
        }else{    return EntityModel.of(post,
                    linkTo(methodOn(PostController.class).postUser(post.getPostId())).withRel("the post owner"),
                   linkTo(methodOn(PostController.class).getAllPostLikes(post.getPostId())).withRel("the post's like"),
                   linkTo(methodOn(PostController.class).getAllPostComments(post.getPostId())).withRel("the post's comment"));
           }
    
          

        } 
         

        public EntityModel<Like> toModelPostLike(Like like)  {
         // User user=null;
            String jwt = parseJwt(request);
              if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                  String username = jwtUtils.getUserNameFromJwtToken(jwt);
              User  user=userRepo.findByUsername(username).get();
             
             Post post = like.post;
     
             if (like.user.getId()==user.getId()){
             return EntityModel.of(like,
             linkTo(methodOn(PostController.class).findById(post.getPostId())).withRel("the post "),
           // linkTo(methodOn(PostController.class).getAllPostLikes(like.getPostId())).withRel("the post's like"),
            linkTo(methodOn(PostController.class).getAllPostComments(post.getPostId())).withRel("the post's comment"),
            linkTo(methodOn(PostController.class).UnCreatelikePost(like.getLikeId())).withRel("delete your like"));
          }else{    return EntityModel.of(like,
            linkTo(methodOn(PostController.class).findById(post.getPostId())).withRel("the post "),
            linkTo(methodOn(PostController.class).getAllPostComments(post.getPostId())).withRel("the post's comment"));   
         }
      
               } return null;   
  
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

  

        public EntityModel<Share> toModelsharepostId(Share share)  {
             User user = userFromToken(request);
    if (user!=null){   
        if (share.user.getId().equals(user.getId())){
           return EntityModel.of(share,
           linkTo(methodOn(PostController.class).findById(share.getPost().getPostId())).withRel("the post you shared"),
          // linkTo(methodOn(PostController.class).getAllPostLikes(share.getPostId())).withRel("the post's like"),
          // linkTo(methodOn(PostController.class).getAllPostComments(share.getPostId(),request)).withRel("the post's comment"),
          linkTo(methodOn(PostController.class).deleteShearById(share.getShareId())).withRel("delete your share post"));
        }else{    return EntityModel.of(share,
                    linkTo(methodOn(PostController.class).findById(share.getPost().getPostId())).withRel("the post you shared"));
                  //  linkTo(methodOn(PostController.class).getAllPostLikes(share.getPostId())).withRel("the post's like"),
                  //  linkTo(methodOn(PostController.class).getAllPostComments(share.getPostId(),request)).withRel("the post's comment"));
    
       } } return EntityModel.of(share,
       linkTo(methodOn(PostController.class).findAllPost()).withRel("Go to the all post"));}


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

