package com.project1.project.Entity.User;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.project1.project.Controllers.Controller;

import jakarta.servlet.http.HttpServletRequest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAss implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User user) {
    
              return EntityModel.of(user,
                linkTo(methodOn(Controller.class).getUserById(user.getId())).withSelfRel());
              //  linkTo(methodOn(Controller.class).addFriend(user.getId())).withRel("Add Friend");

    }
    

public EntityModel<User> toModelfriendself(User user, HttpServletRequest request) {
    Link selfLink = linkTo(methodOn(Controller.class).getUserById(user.getId())).withSelfRel();
    Link addFriendLink = linkTo(methodOn(Controller.class).addFriend(request, user.getId())).withRel("Add Friend");

    return EntityModel.of(user, selfLink, addFriendLink);
}




}
