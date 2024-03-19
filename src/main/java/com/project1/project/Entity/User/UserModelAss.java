package com.project1.project.Entity.User;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.project1.project.Controllers.Controller;
import com.project1.project.Controllers.PostController;

import jakarta.servlet.http.HttpServletRequest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAss implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User user) {
    
              // return EntityModel.of(user,
                // linkTo(methodOn(Controller.class).getUserById(user.getId())).withSelfRel());
              //  linkTo(methodOn(Controller.class).addFriend(user.getId())).withRel("Add Friend");
return null;
    }
    

public EntityModel<User> toModelfriendself(User user, HttpServletRequest request) {
    Link selfLink = linkTo(methodOn(Controller.class).getUserById(user.getId(), request)).withRel("view Profile");
    Link addFriendLink = linkTo(methodOn(Controller.class).addFriend(request, user.getId())).withRel("Add Friend");
    Link deleteFriendLink = linkTo(methodOn(Controller.class).deleteUserFriend( user.getId(), request)).withRel("Remove friend");

    if(user.isFriend())
    return EntityModel.of(user, selfLink, deleteFriendLink);
    return EntityModel.of(user, selfLink, addFriendLink);
  
    
}

public EntityModel<User> toModeluserprofile(User user, HttpServletRequest request) {
  Link addFriendLink = linkTo(methodOn(Controller.class).addFriend(request, user.getId())).withRel("Add Friend");
  Link deleteFriendLink = linkTo(methodOn(Controller.class).deleteUserFriend( user.getId(), request)).withRel("Remove friend");
 Link selfLink = linkTo(methodOn(Controller.class).getUserById(user.getId(), request)).withSelfRel();
  Link userPostLink = linkTo(methodOn(PostController.class).findfriendPosts(request, user.getId())).withRel("posts");

  if(user.isFriend())
  return EntityModel.of(user, selfLink, deleteFriendLink, userPostLink);
  return EntityModel.of(user, selfLink, addFriendLink, userPostLink);



}




}
