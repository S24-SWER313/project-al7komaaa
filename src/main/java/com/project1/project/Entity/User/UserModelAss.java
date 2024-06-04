package com.project1.project.Entity.User;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.project1.project.Controllers.Controller;
import com.project1.project.Controllers.PostController;
import com.project1.project.Security.Jwt.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;

@Component
public class UserModelAss implements RepresentationModelAssembler<User, EntityModel<User>> {
  @Autowired
  private JwtUtils jwtUtils;
  @Autowired
  private UserRepo userRepo;
  
  @Autowired
  HttpServletRequest request;
    @Override
    public EntityModel<User> toModel(User user) {
    
              // return EntityModel.of(user,
                // linkTo(methodOn(Controller.class).getUserById(user.getId())).withSelfRel());
              //  linkTo(methodOn(Controller.class).addFriend(user.getId())).withRel("Add Friend");
return null;
    }
    

public EntityModel<User> toModelfriendself(User user) {
    Link selfLink = linkTo(methodOn(Controller.class).getUserById(user.getId())).withRel("view Profile");
    Link addFriendLink = linkTo(methodOn(Controller.class).addFriend(request, user.getId())).withRel("Add Friend");
    Link deleteFriendLink = linkTo(methodOn(Controller.class).deleteUserFriend( user.getId(), request)).withRel("Remove friend");

    // if(userFromToken(request).friends.contains(user))
    return EntityModel.of(user, selfLink, deleteFriendLink,addFriendLink);
    // return EntityModel.of(user, selfLink, );
  
    
}

public EntityModel<User> toModeluserprofile(User user) {
  Link addFriendLink = linkTo(methodOn(Controller.class).addFriend(request, user.getId())).withRel("Add Friend");
  Link deleteFriendLink = linkTo(methodOn(Controller.class).deleteUserFriend( user.getId(), request)).withRel("Remove friend");
 Link selfLink = linkTo(methodOn(Controller.class).getUserById(user.getId())).withSelfRel();
  Link userPostLink = linkTo(methodOn(PostController.class).findFriendPosts( user.getId())).withRel("posts");

  if(userFromToken(request)!=null||userFromToken(request).friends.contains(user))
  return EntityModel.of(user, selfLink, deleteFriendLink, userPostLink);
  return EntityModel.of(user, selfLink, addFriendLink, userPostLink);



}
public User userFromToken(HttpServletRequest request){
  String jwt = parseJwt(request);
  if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
      String username = jwtUtils.getUserNameFromJwtToken(jwt);
      User user = userRepo.findByUsername(username)
              .orElseThrow(() -> new RuntimeException("User not found"));
            return user;}
  return null;
}
 private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7);
    }

    return null;
  }  




}
